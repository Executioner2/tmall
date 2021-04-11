package com.study.tmall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.tmall.enums.OrderStatusEnum;
import com.study.tmall.exception.TmallException;
import com.study.tmall.model.order.OrderInfo;
import com.study.tmall.model.user.UserInfo;
import com.study.tmall.order.mapper.OrderInfoMapper;
import com.study.tmall.order.service.OrderInfoService;
import com.study.tmall.result.ResultCodeEnum;
import com.study.tmall.vo.order.OrderQueryVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-11 18:06
 * Versions:1.0.0
 * Description:
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {

    /**
     * 分页条件显示订单
     * @param page
     * @param orderQueryVo
     * @return
     */
    @Override
    public IPage<OrderInfo> findPageOrderInfo(Page<OrderInfo> page, OrderQueryVo orderQueryVo) {
        Integer status = orderQueryVo.getStatus(); // 订单状态
        String userId = orderQueryVo.getUserId(); // 用户id
        String outTradeNo = orderQueryVo.getOutTradeNo(); // 订单编号
        Date createDateEnd = orderQueryVo.getCreateDateEnd(); // 订单创建日期结束
        Date createDateBegin = orderQueryVo.getCreateDateBegin(); // 订单创建日期开始

        QueryWrapper<OrderInfo> wrapper = new QueryWrapper<>();
        if (status != null) {
            wrapper.eq("status", status);
        }
        if (StringUtils.isEmpty(userId)) {
            wrapper.eq("user_id", userId);
        }
        if (StringUtils.isEmpty(outTradeNo)) {
            wrapper.eq("out_trade_no", outTradeNo);
        }
        if (createDateBegin != null) {
            wrapper.ge("create_date", createDateBegin);
        }
        if (createDateEnd != null) {
            wrapper.le("create_date", createDateEnd);
        }

        IPage<OrderInfo> orderInfoIPage = baseMapper.selectPage(page, wrapper);
        // 封装参数
        orderInfoIPage.getRecords().stream().forEach(item -> {
            this.packOrderInfo(item);
        });
        return orderInfoIPage;
    }

    /**
     * 订单发货
     * @param id
     */
    @Override
    public void deliverGoods(Integer id) {
        OrderInfo orderInfo = baseMapper.selectById(id);
        if (orderInfo == null) { // 如果查不出来就抛出参数异常
            throw new TmallException(ResultCodeEnum.PARAM_ERROR);
        }
        // 检查订单是否已经付款，处于待发货状态
        Integer orderStatus = orderInfo.getOrderStatus();
        if (orderStatus != OrderStatusEnum.WAIT_SHIPMENTS.getStatus()) {
            throw new TmallException(ResultCodeEnum.DELIVER_GOODS_FAIL);
        }
        // 更新发货时间，更新订单状态
        orderInfo.setDeliveryDate(new Date());
        orderInfo.setOrderStatus(OrderStatusEnum.WAIT_TAKE_GOODS.getStatus());
        baseMapper.updateById(orderInfo);
    }

    /**
     * 封装订单参数
     * @param orderInfo
     */
    private OrderInfo packOrderInfo(OrderInfo orderInfo) {
        Integer orderStatus = orderInfo.getOrderStatus();
        String statusNameByStatus = OrderStatusEnum.getStatusNameByStatus(orderStatus);
        // 订单状态中文显示
        orderInfo.getParams().put("orderStatusStr", statusNameByStatus);
        // TODO 后续根据前端需求继续完善
        return orderInfo;
    }
}
