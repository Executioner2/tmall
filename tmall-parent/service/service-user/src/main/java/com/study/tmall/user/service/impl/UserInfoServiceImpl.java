package com.study.tmall.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.tmall.enums.AuthStatusEnum;
import com.study.tmall.enums.UserLockStatusEnum;
import com.study.tmall.exception.TmallException;
import com.study.tmall.model.user.UserInfo;
import com.study.tmall.result.ResultCodeEnum;
import com.study.tmall.user.mapper.UserInfoMapper;
import com.study.tmall.user.service.UserInfoService;
import com.study.tmall.vo.user.UserQueryVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-11 17:17
 * Versions:1.0.0
 * Description:
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

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
}
