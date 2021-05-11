package com.study.tmall.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.study.tmall.model.user.UserInfo;
import org.apache.ibatis.annotations.Param;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-11 17:15
 * Versions:1.0.0
 * Description:
 */
public interface UserInfoMapper extends BaseMapper<UserInfo> {
    // 根据id设置openid
    void setOpenidById(@Param("id") String id, @Param("openid") String openid);
}
