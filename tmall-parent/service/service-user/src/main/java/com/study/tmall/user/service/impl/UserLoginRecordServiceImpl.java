package com.study.tmall.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.tmall.model.user.UserLoginRecord;
import com.study.tmall.user.mapper.UserLoginRecordMapper;
import com.study.tmall.user.service.UserLoginRecordService;
import org.springframework.stereotype.Service;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-11 17:18
 * Versions:1.0.0
 * Description:
 */
@Service
public class UserLoginRecordServiceImpl extends ServiceImpl<UserLoginRecordMapper, UserLoginRecord> implements UserLoginRecordService {
}
