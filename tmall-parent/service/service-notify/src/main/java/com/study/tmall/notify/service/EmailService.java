package com.study.tmall.notify.service;

import com.study.tmall.dto.DealNotify;
import com.study.tmall.vo.front.EmailCodeVo;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-29 21:36
 * Versions:1.0.0
 * Description:
 */
public interface EmailService {
    // 发送内容到目标邮箱
    void send(String to, String subject, String text);

    // 发送验证码到目标邮箱
    void sendCode(EmailCodeVo emailCodeVo);

    // 删除redis中的邮箱验证码
    void delCode(String key);

    // 订单状态通知
    void dealNotify(DealNotify dealNotify);
}
