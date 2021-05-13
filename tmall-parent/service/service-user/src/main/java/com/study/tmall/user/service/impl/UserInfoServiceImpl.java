package com.study.tmall.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.tmall.enums.AuthStatusEnum;
import com.study.tmall.enums.EmailCodeTypeEnum;
import com.study.tmall.enums.RegularEnum;
import com.study.tmall.enums.UserLockStatusEnum;
import com.study.tmall.exception.TmallException;
import com.study.tmall.model.user.UserInfo;
import com.study.tmall.order.client.OrderFeignClient;
import com.study.tmall.result.ResultCodeEnum;
import com.study.tmall.user.handler.MySource;
import com.study.tmall.user.mapper.UserInfoMapper;
import com.study.tmall.user.service.UserInfoService;
import com.study.tmall.util.*;
import com.study.tmall.vo.front.EmailCodeVo;
import com.study.tmall.vo.front.UserInfoVo;
import com.study.tmall.vo.user.UserLoginVo;
import com.study.tmall.vo.user.UserQueryVo;
import com.study.tmall.vo.user.UserRegisterVo;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-11 17:17
 * Versions:1.0.0
 * Description:
 */
@Service
@EnableBinding(MySource.class)
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
    @Resource
    private MessageChannel codeSend; // 验证码发送
    @Resource
    private MessageChannel codeDelSend; // 验证码删除
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private OrderFeignClient orderFeignClient;

    /**
     * 分页条件显示用户
     * @param page
     * @param userQueryVo
     * @return
     */
    @Override
    public IPage<UserInfo> findPageUserInfo(Page<UserInfo> page, UserQueryVo userQueryVo) {
        String id = userQueryVo.getId(); // 用户id
        String keyword = userQueryVo.getKeyword(); // 用户手机号 昵称 姓名
        Integer status = userQueryVo.getStatus(); // 用户状态
        Integer authStatus = userQueryVo.getAuthStatus(); // 用户认证状态

        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(id)){
            wrapper.eq("id", id);
        }
        if (!StringUtils.isEmpty(keyword)){ // 根据 用户手机号 昵称 姓名 做模糊查询
            wrapper.and(wr->wr.like("name", keyword).or()
                    .like("nick_name", keyword).or()
                    .like("phone", keyword));
        }
        if (status != null){
            wrapper.eq("status", status);
        }
        if (authStatus != null){
            wrapper.eq("auth_status", authStatus);
        }

        IPage<UserInfo> userInfoIPage = baseMapper.selectPage(page, wrapper);
        // 参数封装
        userInfoIPage.getRecords().stream().forEach(item -> {
            this.packageUserInfo(item);
        });
        return userInfoIPage;
    }

    // 参数封装
    private UserInfo packageUserInfo(UserInfo userInfo) {
        Integer authStatus = userInfo.getAuthStatus();
        Integer status = userInfo.getStatus();
        // 把密码值清空
        userInfo.setPassword(null);
        // 把用户状态的中文字符串包装进去
        userInfo.getParams().put("authStatusStr", AuthStatusEnum.getStatusNameByStatus(authStatus));
        userInfo.getParams().put("statusStr", UserLockStatusEnum.getStatusNameByStatus(status));
        return userInfo;
    }

    /**
     * 锁定\解锁 用户（0：锁定  1：正常）
     * @param id
     * @param status
     */
    @Override
    public void lock(String id, Integer status) {
        // 查询数据库是否存在该用户
        UserInfo userInfo = baseMapper.selectById(id);
        if (userInfo == null) {
            throw new TmallException(ResultCodeEnum.PARAM_ERROR);
        }
        // 查询传来的状态是否合法
        if (status!= UserLockStatusEnum.UNLOCK.getStatus().intValue()
                && status != UserLockStatusEnum.LOCK.getStatus().intValue()){
            throw new TmallException(ResultCodeEnum.PARAM_ERROR);
        }
        // 如果传来的status和数据库中一直就不用更新数据库
        if (userInfo.getStatus() == status){
            return;
        }
        // 否则更新锁定状态
        userInfo.setStatus(status);
        baseMapper.updateById(userInfo);
    }

    /**
     * 审批用户认证
     * @param id
     * @param authStatus
     */
    @Override
    public void authUser(String id, Integer authStatus) {
        // 查询数据库是否存在该用户
        UserInfo userInfo = baseMapper.selectById(id);
        if (userInfo == null) {
            throw new TmallException(ResultCodeEnum.PARAM_ERROR);
        }
        // 查询传来的认证状态值是否合法
        if (authStatus != AuthStatusEnum.AUTH_SUCCESS.getStatus().intValue() // 通过认证的值
                && authStatus != AuthStatusEnum.AUTH_FAIL.getStatus().intValue()){ // 不通过认证的值
            throw new TmallException(ResultCodeEnum.PARAM_ERROR);
        }
        // 如果传来的authStatus和数据库中一致就不用更新数据库
        if (userInfo.getAuthStatus() == authStatus){
            return;
        }
        // 否则更新认证状态
        userInfo.setAuthStatus(authStatus);
        baseMapper.updateById(userInfo);
    }

    /**
     * 获取用户基本信息，内部调用
     * @param idList
     * @return
     */
    @Override
    public List<UserInfo> listUserInfo(List<String> idList) {
        List<UserInfo> userInfoList = new ArrayList<>();
        idList.stream().forEach(id -> {
            UserInfo userInfo = baseMapper.selectById(id);
            if (userInfo != null) { // 如果查出来的用户不为空，则把重要信息做置空处理
                userInfo.setPassword(null);
                // TODO 根据未来需求把不需要的参数再进一步置空
            }
            userInfoList.add(userInfo);
        });
        return userInfoList;
    }

    /**
     * 发送邮箱验证码
     * @param userLoginVo
     */
    @Override
    public void sendEmailCode(UserLoginVo userLoginVo) {
        // 对account进行base64解码，这个是前端的base64编码后的，需要知道前端的编码后加密的方式
        String account = Base64.decode(userLoginVo.getAccount());
        // 对密码再进行MD5加密（前端进行了一次加密，后端还要进行加密，数据库中存入的密码是两次加密后的）
        String password = MD5.encrypt(userLoginVo.getPassword());

        // 通过account格式识别出用户以什么方式登录的，已进行相应的数据库查询
        UserInfo userInfo = this.getUserInfoOfLogin(account, password);

        // 查看用户是否被锁定
        if (userInfo.getStatus() == UserLockStatusEnum.LOCK.getStatus()) {
            throw new TmallException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }

        // 获得用户绑定的邮箱，用于发登录验证码
        String email = userInfo.getEmail();
        EmailCodeVo emailCodeVo = new EmailCodeVo();
        emailCodeVo.setEmail(email);
        emailCodeVo.setType(EmailCodeTypeEnum.LOGIN_CODE.getNumber());
        // 发送消息到rabbitMQ队列，内容为邮箱地址
        codeSend.send(MessageBuilder.withPayload(emailCodeVo).build());
    }

    /**
     * 通过account格式识别出用户以什么方式登录的，已进行相应的数据库查询
     * @param account
     * @param password
     * @return
     */
    private UserInfo getUserInfoOfLogin(String account, String password) {
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("password", password);
        UserInfo userInfo;
        if (Pattern.matches(RegularEnum.USER_NAME_LOGIN.getRegex(), account)) {
            // 是用户名登录，就 用户名+密码 查询数据库
            wrapper.eq("name", account);
            userInfo = baseMapper.selectOne(wrapper);
            if (userInfo == null) {
                throw new TmallException(ResultCodeEnum.LOGIN_NAME_FAIL);
            }
        } else if(Pattern.matches(RegularEnum.USER_PHONE_LOGIN.getRegex(), account)) {
            // 是手机号登录，就 手机号+密码 查询数据库
            wrapper.eq("phone", account);
            userInfo = baseMapper.selectOne(wrapper);
            if (userInfo == null) {
                throw new TmallException(ResultCodeEnum.LOGIN_PHONE_FAIL);
            }
        } else if(Pattern.matches(RegularEnum.USER_EMAIL_LOGIN.getRegex(), account)) {
            // 是邮箱登录，就 邮箱+密码 查询数据库
            wrapper.eq("email", account);
            userInfo = baseMapper.selectOne(wrapper);
            if (userInfo == null) {
                throw new TmallException(ResultCodeEnum.LOGIN_EMAIL_FAIL);
            }
        } else {
            // 如果都不满足，就用户名和密码错误
            throw new TmallException(ResultCodeEnum.LOGIN_NAME_FAIL);
        }
        return userInfo;
    }

    /**
     * 注册发送验证码到邮箱
     * @param email
     */
    @Override
    public void sendEmailCode(String email) {
        // 对邮箱地址进行解码
        String decode = Base64.decode(email);
        EmailCodeVo emailCodeVo = new EmailCodeVo();
        emailCodeVo.setEmail(decode);
        emailCodeVo.setType(EmailCodeTypeEnum.REGISTER_CODE.getNumber());
        // 发送到rabbitMQ中
        codeSend.send(MessageBuilder.withPayload(emailCodeVo).build());
    }

    /**
     * 用户登录
     * @param userLoginVo
     */
    @Override
    public String userLogin(UserLoginVo userLoginVo) {
        // 对account进行base64解码，这个是前端的base64编码后的，需要知道前端的编码后加密的方式
        String account = Base64.decode(userLoginVo.getAccount());
        // 对验证码进行base64解码
        String emailCode = Base64.decode(userLoginVo.getEmailCode());
        // 对密码再进行MD5加密（前端进行了一次加密，后端还要进行加密，数据库中存入的密码是两次加密后的）
        String password = MD5.encrypt(userLoginVo.getPassword());

        // 通过account格式识别出用户以什么方式登录的，已进行相应的数据库查询
        UserInfo userInfo = this.getUserInfoOfLogin(account, password);
        String key = EmailCodeTypeEnum.LOGIN_CODE.getType() + userInfo.getEmail();

        // 从redis中查询出验证码
        String code = stringRedisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(code) || !code.equals(emailCode)) {
            throw new TmallException(ResultCodeEnum.CODE_ERROR);
        }

        // 查看用户是否被锁定
        if (userInfo.getStatus() == UserLockStatusEnum.LOCK.getStatus()) {
            throw new TmallException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }

        // 创建token
        String token = JwtHelper.createToken(userInfo.getId(), userInfo.getPassword());

        // 登录成功后应该删除redis中的邮箱验证码（发送到rabbit中）
        codeDelSend.send(MessageBuilder.withPayload(key).build());
        return token;
    }

    /**
     * 用户名重复检测
     * @param name
     * @return
     */
    @Override
    public Boolean userNameRepeatCheck(String name) {
        // 前端传来的是经过了base64编码并加密了的，先对用户名进行解密解码
        String account = Base64.decode(name);
        // 查询数据库
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("name", account);
        Integer integer = baseMapper.selectCount(wrapper);
        // 如果查询结果不为0，则用户名已经重复，返回true
        if (integer != 0) {
            return true;
        }
        return false;
    }

    /**
     * 检测邮箱是否被使用
     * @param email
     * @return
     */
    @Override
    public Boolean userEmailRepeatCheck(String email) {
        // 前端传来的是经过了base64编码并加密了的，先对邮箱地址进行解密解码
        String decodeEmail = Base64.decode(email);
        // 查询数据库
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("email", decodeEmail);
        Integer integer = baseMapper.selectCount(wrapper);
        // 如果查询结果不为0，则邮箱地址已经重复，返回true
        if (integer != 0) {
            return true;
        }
        return false;
    }

    /**
     * 用户注册
     * @param userRegisterVo
     */
    @Override
    public void userRegister(UserRegisterVo userRegisterVo) {
        // 对用户名，邮箱，验证码 进行解码
        String name = Base64.decode(userRegisterVo.getName());
        String email = Base64.decode(userRegisterVo.getEmail());
        String emailCode = Base64.decode(userRegisterVo.getEmailCode());

        // 从redis中查询验证码是否正确
        String code = stringRedisTemplate.opsForValue()
                .get(EmailCodeTypeEnum.REGISTER_CODE.getType() + email);
        if (StringUtils.isEmpty(emailCode) || !emailCode.equals(code)) {
            throw new TmallException(ResultCodeEnum.CODE_ERROR);
        }

        // 对密码再进行一次加密
        String password = MD5.encrypt(userRegisterVo.getPassword());

        // 设置实体类
        UserInfo userInfo = new UserInfo();
        userInfo.setName(name);
        userInfo.setEmail(email);
        userInfo.setPassword(password);
        // 写入到数据库中去
        baseMapper.insert(userInfo);
    }

    /**
     * 根据openid获取用户信息
     * @param openid
     * @return
     */
    @Override
    public UserInfo getByOpenid(String openid) {
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("openid", openid);
        return baseMapper.selectOne(wrapper);
    }

    /**
     * 用户邮箱绑定
     * @param token
     * @param userRegisterVo
     */
    @Override
    public String emailBinding(String token, UserRegisterVo userRegisterVo) {
        // 解析token
        String userId = JwtHelper.getUserId(token);

        // 对用户名，邮箱，验证码 进行解码
        String name = Base64.decode(userRegisterVo.getName());
        String email = Base64.decode(userRegisterVo.getEmail());
        String emailCode = Base64.decode(userRegisterVo.getEmailCode());

        // 从redis中查询验证码是否正确
        String code = stringRedisTemplate.opsForValue()
                .get(EmailCodeTypeEnum.REGISTER_CODE.getType() + email);
        if (StringUtils.isEmpty(emailCode) || !emailCode.equals(code)) {
            throw new TmallException(ResultCodeEnum.CODE_ERROR);
        }

        // 对密码再进行一次加密
        String password = MD5.encrypt(userRegisterVo.getPassword());

        // 设置实体类
        UserInfo userInfo = new UserInfo();
        userInfo.setId(userId);
        userInfo.setName(name);
        userInfo.setEmail(email);
        userInfo.setPassword(password);
        // 写入到数据库中去
        baseMapper.updateById(userInfo);

        // 返回一个全新的token
        String newToken = JwtHelper.createToken(userId, password);
        return newToken;
    }

    /**
     * 根据token获取用户信息
     * @param token
     * @return
     */
    @Override
    public UserInfoVo getUserInfoByToken(String token) {
        // 获取用户id和用户密码
        String userId = JwtHelper.getUserId(token);
        String password = JwtHelper.getPassword(token);

        // 根据id和密码进行查询
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("id", userId);
        wrapper.eq("password", password);
        UserInfo userInfo = baseMapper.selectOne(wrapper);
        if (userInfo == null) {
            throw new TmallException(ResultCodeEnum.FETCH_USERINFO_ERROR);
        }

        // 对用户数据进行包装
        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setId(userId);
        // 拿取用户名称
        String name = this.getName(userInfo);
        userInfoVo.setName(name);
        // 远程调用获得用户购物车商品数量
        Integer number = orderFeignClient.getProductNumberByUserId(userId);
        userInfoVo.setProductNumber(number);

        return userInfoVo;
    }

    /**
     * 根据token获取用户详情信息
     * @param token
     * @return
     */
    @Override
    public UserInfo getUserDetailsInfoByToken(String token) {
        // 获取用户id和用户密码
        String userId = JwtHelper.getUserId(token);
        String password = JwtHelper.getPassword(token);

        // 根据id和密码进行查询
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("id", userId);
        wrapper.eq("password", password);
        UserInfo userInfo = baseMapper.selectOne(wrapper);
        if (userInfo == null) {
            throw new TmallException(ResultCodeEnum.FETCH_USERINFO_ERROR);
        }
        // 密码 id 置空
        userInfo.setPassword(null);
        userInfo.setId(null);

        return userInfo;
    }

    /**
     * 解除微信绑定
     * @param token
     */
    @Override
    public void unWechatBinding(String token) {
        String userId = JwtHelper.getUserId(token);
        // 根据id设置openid
        baseMapper.setOpenidById(userId, null);
    }

    /**
     * 修改用户昵称
     * @param token
     * @param nickName
     */
    @Override
    public void updateNickName(String token, String nickName) {
        String userId = JwtHelper.getUserId(token);
        UserInfo userInfo = baseMapper.selectById(userId);
        userInfo.setNickName(nickName);
        baseMapper.updateById(userInfo);
    }

    /**
     * 用户头像上传
     * @param token
     * @param file
     */
    @Override
    public void uploadAvatar(String token, MultipartFile file) {
        try {
            String userId = JwtHelper.getUserId(token);
            // 查询数据库
            UserInfo userInfo = baseMapper.selectById(userId);

            // 先拿到原来的头像，等新头像写入到数据库后再删除旧的头像
            String oldAvatar = userInfo.getAvatar();

            // 写入到fastDFS中
            String filename = file.getOriginalFilename();
            String[] upload = FastDFSUtil.upload(file.getBytes(), filename);
            String avatarUrl = ImageUtil.compoundUrl(upload); // 头像图片访问地址

            // 写入数据库中
            userInfo.setAvatar(avatarUrl);
            baseMapper.updateById(userInfo);

            // 如果头像不等于空那么还要删除原本的头像
            if (!StringUtils.isEmpty(oldAvatar)) {
                // 取得图片 服务器协议+ip  group  fastDFS中的路径
                String[] strings = ImageUtil.splitUrl(oldAvatar);
                // 传入group和fastDFS中的路径 删除fastDFS中的图片
                FastDFSUtil.delete(strings[1], strings[2]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // 拿取用户名称
    private String getName(UserInfo userInfo) {
        if (!StringUtils.isEmpty(userInfo.getNickName())) {
            // 如果昵称不为空则返回昵称
            return userInfo.getNickName();
        } else {
            // 否则返回会员名
            return userInfo.getName();
        }
    }

}
