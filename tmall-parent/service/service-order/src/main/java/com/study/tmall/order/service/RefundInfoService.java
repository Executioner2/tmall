package com.study.tmall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.study.tmall.model.order.RefundInfo;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-11 18:08
 * Versions:1.0.0
 * Description:
 */
public interface RefundInfoService extends IService<RefundInfo> {
    // 根据订单id删除退款记录
    void removeByOrderId(String id);
}
