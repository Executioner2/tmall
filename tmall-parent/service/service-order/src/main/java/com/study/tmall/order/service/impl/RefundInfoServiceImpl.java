package com.study.tmall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.tmall.model.order.RefundInfo;
import com.study.tmall.order.mapper.RefundInfoMapper;
import com.study.tmall.order.service.RefundInfoService;
import org.springframework.stereotype.Service;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-11 18:08
 * Versions:1.0.0
 * Description:
 */
@Service
public class RefundInfoServiceImpl extends ServiceImpl<RefundInfoMapper, RefundInfo> implements RefundInfoService {

    /**
     * 根据订单id删除退款记录
     * @param id
     */
    @Override
    public void removeByOrderId(String id) {
        QueryWrapper<RefundInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id", id);
        baseMapper.delete(wrapper);
    }
}
