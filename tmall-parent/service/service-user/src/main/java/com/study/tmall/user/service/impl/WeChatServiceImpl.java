package com.study.tmall.user.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.study.tmall.enums.WeChatRedirectTypeEnum;
import com.study.tmall.model.user.UserInfo;
import com.study.tmall.result.ResultCodeEnum;
import com.study.tmall.user.service.UserInfoService;
import com.study.tmall.user.service.WeChatService;
import com.study.tmall.user.util.ConstantWxPropertiesUtil;
import com.study.tmall.user.util.HttpClientUtils;
import com.study.tmall.util.Base64;
import com.study.tmall.util.JwtHelper;
import com.study.tmall.util.TokenUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-05-06 20:25
 * Versions:1.0.0
 * Description:
 */
@Service
public class WeChatServiceImpl implements WeChatService {
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 获取微信登录二维码
     * @param type 回调类型，映射回调地址
     * @return
     */
    @Override
    public Map<String, String> weChatQRCode(Integer type) {
        try {
            Map<String, String> map = new HashMap<>();
            String encode = "";
            // 把回调地址进行utf-8编码
            if (type != null) {
                if (type == WeChatRedirectTypeEnum.LOGIN.getType()) { // 微信登录回调地址
                    encode = URLEncoder.encode(ConstantWxPropertiesUtil.WX_OPEN_REDIRECT_URL, "utf-8");
                } else if (type == WeChatRedirectTypeEnum.BINDING.getType()) { // 微信绑定回调地址
                    encode = URLEncoder.encode(ConstantWxPropertiesUtil.WX_OPEN_BINDING_URL, "utf-8");
                }
            }
            // 生成一个uuid，便于向redis进行轮询，判别用户是否扫码
            String uuid = IdUtil.simpleUUID();
            // 对uuid进行base64编码并进行加密
            uuid = Base64.encode(uuid);

            // 拼接二维码url地址
            /**
             * appid: 测试号有提供
             * 	redirect_uri: 扫码成功后的回调地址，就自己些的那个api接口的地址
             * 	response_type: 响应类型，直接就二维码链接，所以填code
             * 	scope: 如果是静默登录（用户无感知）就填snsapi_userinfo
             * 	state: 搞个uuid，以便于在redis中做轮询查询
             * 	#wechat_redirect：无参数，就写在url最末尾
             *
             * 微信官方例子：
             *  https://open.weixin.qq.com/connect/oauth2/authorize
             *      ?appid=wxf0e81c3bee622d60
             *      &redirect_uri=http%3A%2F%2Fnba.bluewebgame.com%2Foauth_response.php
             *      &response_type=code
             *      &scope=snsapi_userinfo
             *      &state=STATE
             *      #wechat_redirect
             */
            String url = "https://open.weixin.qq.com/connect/oauth2/authorize"
                    + "?appid=" + ConstantWxPropertiesUtil.WX_OPEN_APP_ID
                    + "&redirect_uri=" + encode
                    + "&response_type=code"
                    + "&scope=snsapi_userinfo"  // snsapi_userinfo 是公众号的scope
                    + "&state=" + uuid  // 官方说搞一个state要安全些，值可以是随机一个字符串，这里用uuid，为了方便查redis
                    + "#wechat_redirect";

            map.put("QRCodeUrl", url);
            map.put("uuid", uuid);
            return map;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 用户扫码成功后的回调接口
     * @param code
     * @param state
     * @return
     */
    @Override
    public Boolean callback(String code, String state) {
        try {
            Map<String, Object> map = new HashMap<>();
            // 对state（base64编码并加密后的uuid）进行解密解码
            state = Base64.decode(state);
            /**
             * 通过code获取accessToken的官方案例：
             *  https://api.weixin.qq.com/sns/oauth2/access_token
             *      ?appid=APPID
             *      &secret=SECRET
             *      &code=CODE
             *      &grant_type=authorization_code
             */

            // 因为每次的code不一样，所以拼接通用请求地址接收内存空间
            StringBuffer baseAccessTokenUrl = new StringBuffer()
                    .append("https://api.weixin.qq.com/sns/oauth2/access_token")
                    .append("?appid=%s")
                    .append("&secret=%s")
                    .append("&code=%s")
                    .append("&grant_type=authorization_code"); // 固定参数
            // 格式化请求地址
            String accessTokenUrl = String.format(baseAccessTokenUrl.toString(),
                    ConstantWxPropertiesUtil.WX_OPEN_APP_ID,
                    ConstantWxPropertiesUtil.WX_OPEN_APP_SECRET,
                    code);
            // 向获取accessToken的url地址发送请求
            String jsonString = HttpClientUtils.get(accessTokenUrl);
            JSONObject jsonObject = JSONObject.parseObject(jsonString);
            String access_token = jsonObject.getString("access_token");
            String openid = jsonObject.getString("openid");

            // 根据获取到的access_token获取用户详情信息
            // 先根据openId查询数据库，如果数据库为空才去微信请求获取
            UserInfo userInfo = userInfoService.getByOpenid(openid);
            if (userInfo == null) {
                // 请求微信获取用户信息
                StringBuffer baseUserInfoUrl = new StringBuffer()
                        .append("https://api.weixin.qq.com/sns/userinfo")
                        .append("?access_token=%s")
                        .append("&openid=%s")
                        .append("&lang=zh_CN");
                String getUserInfoUrl = String.format(baseUserInfoUrl.toString(), access_token, openid);
                String resultInfo = HttpClientUtils.get(getUserInfoUrl);
                JSONObject resultUserInfoJson = JSONObject.parseObject(resultInfo);
                // 解析用户信息
                String nickname = resultUserInfoJson.getString("nickname"); // 用户昵称
                //String headimgurl = resultUserInfoJson.getString("headimgurl"); //用户头像
                // 写入到数据库中
                userInfo = new UserInfo();
                userInfo.setOpenid(openid);
                userInfo.setNickName(nickname);
                userInfoService.save(userInfo);
                // 邮箱未绑定，传入前端，前端跳转到邮箱绑定页面
                map.put("state", ResultCodeEnum.EMAIL_UNBIND.getCode());
            } else {
                if (StringUtils.isEmpty(userInfo.getEmail())) {
                    // 邮箱未绑定，传入前端，前端跳转到邮箱绑定页面
                    map.put("state", ResultCodeEnum.EMAIL_UNBIND.getCode());
                } else {
                    // 邮箱已绑定，传入前端，前端跳转到首页
                    map.put("state", ResultCodeEnum.EMAIL_BIND.getCode());
                }
            }
            // 做成token存入redis，有效时间为60s
            String token = TokenUtil.createToken(userInfo.getId(), userInfo.getPassword());
            map.put("token", token);
            redisTemplate.opsForValue().set(state, map, 60, TimeUnit.MINUTES);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 用户登录扫码状态轮询
     * @param state
     * @return
     */
    @Override
    public Map<String, Object> polling(String state) {
        // 对state（base64编码并加密后的uuid）进行解密解码
        state = Base64.decode(state);
        Map<String, Object> map = (Map<String, Object>) redisTemplate.opsForValue().get(state);
        return map;
    }

    /**
     * 微信绑定回调函数
     * @param code
     * @param state
     * @return
     */
    @Override
    public Boolean bindingCallback(String code, String state) {
        try {
            Map<String, String> map = new HashMap<>();
            // 对state（base64编码并加密后的uuid）进行解密解码
            state = Base64.decode(state);

            // 因为每次的code不一样，所以拼接通用请求地址接收内存空间
            StringBuffer baseAccessTokenUrl = new StringBuffer()
                    .append("https://api.weixin.qq.com/sns/oauth2/access_token")
                    .append("?appid=%s")
                    .append("&secret=%s")
                    .append("&code=%s")
                    .append("&grant_type=authorization_code"); // 固定参数
            // 格式化请求地址
            String accessTokenUrl = String.format(baseAccessTokenUrl.toString(),
                    ConstantWxPropertiesUtil.WX_OPEN_APP_ID,
                    ConstantWxPropertiesUtil.WX_OPEN_APP_SECRET,
                    code);
            // 向获取accessToken的url地址发送请求
            String jsonString = HttpClientUtils.get(accessTokenUrl);
            JSONObject jsonObject = JSONObject.parseObject(jsonString);
            String access_token = jsonObject.getString("access_token");
            String openid = jsonObject.getString("openid");

            // 装入map中，然后再写入redis中
            map.put("access_token", access_token);
            map.put("openid", openid);
            redisTemplate.opsForValue().set(state, map, 120, TimeUnit.MINUTES);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 微信绑定轮询
     * @param state
     * @return
     */
    @Override
    public Boolean pollingBinding(String state) {
        // 对state（base64编码并加密后的uuid）进行解密解码
        state = Base64.decode(state);
        Object o = redisTemplate.opsForValue().get(state);
        return o == null ? false : true;
    }

    /**
     * 确认进行微信绑定
     * @param token
     * @param state
     * @return
     */
    @Override
    public Boolean confirmWeChatBinding(String token, String state) {
        try {
            String userId = TokenUtil.getUserId(token);
            // 对state（base64编码并加密后的uuid）进行解密解码
            state = Base64.decode(state);
            // 根据state查询出openid和account_token
            Map<String, String> map = (Map<String, String>) redisTemplate.opsForValue().get(state);
            String access_token = map.get("access_token");
            String openid = map.get("openid");

            // 绑定到数据库
            UserInfo userInfo = userInfoService.getById(userId);
            userInfo.setOpenid(openid); // 设置openid
            // 如果用户没有设置昵称，那么就把微信昵称当作用户昵称
            if (StringUtils.isEmpty(userInfo.getNickName())) {
                // 请求微信获取用户信息
                StringBuffer baseUserInfoUrl = new StringBuffer()
                        .append("https://api.weixin.qq.com/sns/userinfo")
                        .append("?access_token=%s")
                        .append("&openid=%s")
                        .append("&lang=zh_CN");
                String getUserInfoUrl = String.format(baseUserInfoUrl.toString(), access_token, openid);

                String resultInfo = HttpClientUtils.get(getUserInfoUrl);
                JSONObject resultUserInfoJson = JSONObject.parseObject(resultInfo);
                // 解析用户信息
                String nickname = resultUserInfoJson.getString("nickname"); // 用户昵称
                //String headimgurl = resultUserInfoJson.getString("headimgurl"); //用户头像

                userInfo.setNickName(nickname);
            }
            // 更新数据库
            userInfoService.updateById(userInfo);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
