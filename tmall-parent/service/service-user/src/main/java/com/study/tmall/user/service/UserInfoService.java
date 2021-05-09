package com.study.tmall.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.study.tmall.model.user.UserInfo;
import com.study.tmall.vo.front.UserInfoVo;
import com.study.tmall.vo.user.UserLoginVo;
import com.study.tmall.vo.user.UserQueryVo;
import com.study.tmall.vo.user.UserRegisterVo;

import java.util.List;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-11 17:17
 * Versions:1.0.0
 * Description:
 */
public interface UserInfoService extends IService<UserInfo> {
    // 分页条件显示用户
    IPage<UserInfo> findPageUserInfo(Page<UserInfo> page, UserQueryVo userQueryVo);

    // 锁定\解锁 用户
    void lock(String id, Integer status);

    // 审批用户认证
    void authUser(String id, Integer authStatus);

    // 获取用户基本信息，内部调用
    List<UserInfo> listUserInfo(List<String> idList);

    // 发送邮箱验证码
    void sendEmailCode(UserLoginVo userLoginVo);

    // 发送邮箱验证码
    void sendEmailCode(String email);

    // 用户登录
    String userLogin(UserLoginVo userLoginVo);

    // 用户名重复检测
    Boolean userNameRepeatCheck(String name);

    // 检测邮箱是否被使用
    Boolean userEmailRepeatCheck(String email);

    // 用户注册
    void userRegister(UserRegisterVo userRegisterVo);

    // 根据openid获取用户信息
    UserInfo getByOpenid(String openid);

    // 用户邮箱绑定
    String emailBinding(String token, UserRegisterVo userRegisterVo);

    // 根据token获取用户信息
    UserInfoVo getUserInfoByToken(String token);
}
