package com.study.tmall.order.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.tmall.enums.AuthStatusEnum;
import com.study.tmall.enums.OrderStatusEnum;
import com.study.tmall.enums.UserLockStatusEnum;
import com.study.tmall.exception.TmallException;
import com.study.tmall.model.order.OrderInfo;
import com.study.tmall.model.order.OrderItem;
import com.study.tmall.model.product.ProductInfo;
import com.study.tmall.model.user.UserInfo;
import com.study.tmall.order.mapper.OrderInfoMapper;
import com.study.tmall.order.service.OrderInfoService;
import com.study.tmall.order.service.OrderItemService;
import com.study.tmall.result.ResultCodeEnum;
import com.study.tmall.user.client.UserFeignClient;
import com.study.tmall.util.JwtHelper;
import com.study.tmall.vo.order.OrderQueryVo;
import com.study.tmall.vo.order.SettlementVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-11 18:06
 * Versions:1.0.0
 * Description:
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {
    @Resource
    private UserFeignClient userFeignClient;
    @Resource
    private OrderItemService orderItemService;


    /**
     * 分页条件显示订单
     * @param page
     * @param orderQueryVo
     * @return
     */
    @Override
    public IPage<OrderInfo> findPageOrderInfo(Page<OrderInfo> page, OrderQueryVo orderQueryVo) {
        Integer orderStatus = orderQueryVo.getOrderStatus(); // 订单状态
        String userId = orderQueryVo.getUserId(); // 用户id
        String outTradeNo = orderQueryVo.getOutTradeNo(); // 订单编号
        Date createDateEnd = orderQueryVo.getCreateDateEnd(); // 订单创建日期结束
        Date createDateBegin = orderQueryVo.getCreateDateBegin(); // 订单创建日期开始

        QueryWrapper<OrderInfo> wrapper = new QueryWrapper<>();
        if (orderStatus != null) {
            wrapper.eq("order_status", orderStatus);
        }
        if (!StringUtils.isEmpty(userId)) {
            wrapper.eq("user_id", userId);
        }
        if (!StringUtils.isEmpty(outTradeNo)) {
            wrapper.eq("out_trade_no", outTradeNo);
        }
        if (createDateBegin != null) {
            wrapper.ge("create_date", createDateBegin);
        }
        if (createDateEnd != null) {
            wrapper.le("create_date", createDateEnd);
        }

        IPage<OrderInfo> orderInfoIPage = baseMapper.selectPage(page, wrapper);
        // 每个订单都要获得用户信息，为了减少远程调用量，把这一页所有订单的用户id封装到list集合中
        List<String> userIdList = new ArrayList<>();
        orderInfoIPage.getRecords().stream().forEach(item -> {
            userIdList.add(item.getUserId());
        });
        // 通过远程调用获得用户信息集合
        List<UserInfo> userInfoList = userFeignClient.listUserInfoOfInner(userIdList);

        // 封装参数
        orderInfoIPage.getRecords().stream().forEach(item -> {
            this.packOrderInfo(item, userInfoList);
        });
        return orderInfoIPage;
    }


    /**
     * 订单发货
     * @param id
     */
    @Override
    public void deliverGoods(String id) {
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
    private OrderInfo packOrderInfo(OrderInfo orderInfo, List<UserInfo> userInfoList) {
        Integer number = 0; // 商品总数量
        BigDecimal totalMoney = new BigDecimal(0);
        Integer orderStatus = orderInfo.getOrderStatus();
        String statusNameByStatus = OrderStatusEnum.getStatusNameByStatus(orderStatus);
        // 获取订单项信息
        List<OrderItem> orderItems = orderItemService.showByOrderId(orderInfo.getId());
        // 封装总数量 和 总金额
        for (OrderItem obj : orderItems) {
            ProductInfo productInfo = (ProductInfo) obj.getParams().get("productInfo");
            number += obj.getNumber();
            totalMoney = totalMoney.add(productInfo.getPromotePrice());
        }

        // 额外数据封装
        orderInfo.getParams().put("number", number); // 商品总数量
        orderInfo.getParams().put("totalMoney", totalMoney); // 商品总金额
        orderInfo.getParams().put("orderItems", orderItems); // 订单项
        orderInfo.getParams().put("orderStatusStr", statusNameByStatus); // 订单状态中文显示

        // 把用户信息封装进去
        if (orderInfo.getUserId() != null) {
            Iterator<UserInfo> it = userInfoList.iterator();
            while (it.hasNext()) {
                UserInfo userInfo = it.next();
                if (orderInfo.getUserId().equals(userInfo.getId())) {
                    if (!StringUtils.isEmpty(userInfo.getNickName())) { // 如果昵称不为空就把昵称做userName
                        orderInfo.getParams().put("customerName", userInfo.getNickName());
                    } else if (!StringUtils.isEmpty(userInfo.getPhone())) { // 如果手机号不为空就把手机号做userName
                        orderInfo.getParams().put("customerName", userInfo.getPhone());
                    } else { // 否则把用户名做userName
                        orderInfo.getParams().put("customerName", userInfo.getName());
                    }
                    break;
                }
            }
        }
        // TODO 后续根据前端需求继续完善
        return orderInfo;
    }

    /**
     * 下单
     * @param token
     * @param settlementVo
     * @return
     */
    @Override
    public String settlement(String token, SettlementVo settlementVo) {
        // 校验用户合法性
        UserInfo userInfo = userFeignClient.getUserInfoByToken(token);
        // 是否查询到用户
        if (userInfo == null) {
            throw new TmallException(ResultCodeEnum.LOGIN_AURH);
        }
        // 用户是否被禁用
        if (userInfo.getStatus() == UserLockStatusEnum.LOCK.getStatus()) {
            throw new TmallException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }
        // 用户是否实名认证
        if (userInfo.getAuthStatus() != AuthStatusEnum.AUTH_SUCCESS.getStatus()) {
            throw new TmallException(ResultCodeEnum.NOT_AUTH);
        }

        // 提取settlementVo参数，设置订单参数
        OrderInfo orderInfo = new OrderInfo();
        BeanUtils.copyProperties(settlementVo, orderInfo);
        // 订单项id集合
        List<String> orderItemIdList = settlementVo.getOrderItemIdList();
        // 查询出所有订单项和商品，进行支付金额计算
        List<OrderItem> orderItems = orderItemService.selectByIdList(orderItemIdList);
        if (orderItems == null) {
            throw new TmallException(ResultCodeEnum.PARAM_ERROR);
        }

        // 遍历计算总金额
        BigDecimal amount = new BigDecimal("0");
        for (OrderItem item : orderItems) {
            BigDecimal number = new BigDecimal(item.getNumber());
            ProductInfo productInfo = (ProductInfo) item.getParams().get("productInfo");
            BigDecimal promotePrice = productInfo.getPromotePrice();
            amount = amount.add(promotePrice.multiply(number));
        }

        orderInfo.setOrderStatus(OrderStatusEnum.WAIT_PAY.getStatus()); // 设置订单状态
        Date date = new Date(); // 下单日期
        orderInfo.setCreateDate(date); // 设置下单日期时间
        orderInfo.setUserId(userInfo.getId()); // 设置用户id
        orderInfo.setAmount(amount); // 支付金额
        // 生成流水订单号
        String format = DateUtil.format(date,"yyyyMMddHHmmss");
        String numbers = RandomUtil.randomNumbers(5);
        orderInfo.setOutTradeNo(format + numbers); // 对外订单号

        // 生成订单
        baseMapper.insert(orderInfo);
        orderItemService.relevanceOrderInfo(orderItemIdList, orderInfo.getId()); // 让订单项关联订单id

        return orderInfo.getId();
    }

    /**
     * 根据条件显示用户订单
     * @param token
     * @param orderStatus
     * @return
     */
    @Override
    public List<OrderInfo> listOrderInfo(String token, Integer orderStatus) {
        // 先验证用户信息是否合法
        UserInfo userInfo = userFeignClient.getUserInfoByToken(token);
        if (userInfo == null) throw new TmallException(ResultCodeEnum.FETCH_USERINFO_ERROR);

        // 根据用户id和订单状态查询出用户订单
        QueryWrapper<OrderInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userInfo.getId());
        if (orderStatus != -1) {
            wrapper.eq("order_status", orderStatus);
        }
        wrapper.orderByDesc("create_date"); // 降序排序
        List<OrderInfo> orderInfos = baseMapper.selectList(wrapper);
        // 如果订单集合为空或者没有数据则返回空
        if (orderInfos == null || orderInfos.size() == 0) {
            return null;
        }

        List<String> orderIdList = new ArrayList<>();
        // 取出订单id和字符化订单状态
        orderInfos.stream().forEach(item -> {
            orderIdList.add(item.getId());
            item.getParams().put("orderStatusStr", OrderStatusEnum.getStatusNameByStatus(item.getOrderStatus()));
        });

        // 根据订单id取得订单项信息
        Map<String, List<OrderItem>> orderItemMap = orderItemService.getOrderItemByOrderId(orderIdList);
        // 把订单项信息封装到订单集合中
        orderInfos.stream().forEach(item -> {
            List<OrderItem> orderItems = orderItemMap.get(item.getId());
            // 计算一个订单中的商品数量
            int totalNumber = 0;
            for (OrderItem obj : orderItems) {
                totalNumber += obj.getNumber();
            }
            item.getParams().put("totalNumber", totalNumber);
            item.getParams().put("orderItems", orderItems);
        });

        return orderInfos;
    }

    /**
     * 获取订单信息
     * @param token
     * @param orderId
     * @return
     */
    @Override
    public OrderInfo getOrderInfo(String token, String orderId) {
        // 先验证用户信息是否合法
        UserInfo userInfo = userFeignClient.getUserInfoByToken(token);
        if (userInfo == null) throw new TmallException(ResultCodeEnum.FETCH_USERINFO_ERROR);
        // 根据用户id 订单id 订单状态（待付款）查询订单信息
        OrderInfo orderInfo = this.getOrderInfoOfCondition(userInfo, orderId, OrderStatusEnum.WAIT_PAY.getStatus());

        return orderInfo;
    }

    // 根据用户id 订单id 订单状态查询订单信息
    private OrderInfo getOrderInfoOfCondition(UserInfo userInfo, String orderId, Integer status) {
        QueryWrapper<OrderInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("id", orderId);
        wrapper.eq("user_id", userInfo.getId());
        wrapper.eq("order_status", status);
        OrderInfo orderInfo = baseMapper.selectOne(wrapper);

        return orderInfo;
    }

    /**
     * 用户催卖家发货，卖家光速发货
     * @param token
     * @param orderId
     */
    @Override
    public void deliverGoodsByUser(String token, String orderId) {
        // 先验证用户信息是否合法
        UserInfo userInfo = userFeignClient.getUserInfoByToken(token);
        if (userInfo == null) throw new TmallException(ResultCodeEnum.FETCH_USERINFO_ERROR);
        // 根据用户id 订单id 订单状态（待发货）查询订单信息
        OrderInfo orderInfo = this.getOrderInfoOfCondition(userInfo, orderId, OrderStatusEnum.WAIT_SHIPMENTS.getStatus());
        // 查询结果为空，参数不正确
        if (orderInfo == null) throw new TmallException(ResultCodeEnum.PARAM_ERROR);

        // 如果不为空，那么更新订单信息
        orderInfo.setOrderStatus(OrderStatusEnum.WAIT_TAKE_GOODS.getStatus()); // 待收货
        orderInfo.setDeliveryDate(new Date()); // 更新发货日期
        baseMapper.updateById(orderInfo);
    }

    /**
     * 获取订单详细信息
     * @param token
     * @param orderId
     * @return
     */
    @Override
    public OrderInfo getOrderInfoDetails(String token, String orderId) {
        // 先验证用户信息是否合法
        UserInfo userInfo = userFeignClient.getUserInfoByToken(token);
        if (userInfo == null) throw new TmallException(ResultCodeEnum.FETCH_USERINFO_ERROR);
        // 根据用户id 订单id 订单状态（待收货）查询订单信息
        OrderInfo orderInfo = this.getOrderInfoOfCondition(userInfo, orderId, OrderStatusEnum.WAIT_TAKE_GOODS.getStatus());
        // 查询结果为空，参数不正确
        if (orderInfo == null) throw new TmallException(ResultCodeEnum.PARAM_ERROR);

        // 往订单信息中封装订单项信息
        List<OrderItem> orderItems = orderItemService.getOrderItemByOrderId(orderId);
        orderInfo.getParams().put("orderItems", orderItems);
        return orderInfo;
    }

    /**
     * 确认收货
     * @param token
     * @param orderId
     */
    @Override
    public void confirmReceipt(String token, String orderId) {
        // 先验证用户信息是否合法
        UserInfo userInfo = userFeignClient.getUserInfoByToken(token);
        if (userInfo == null) throw new TmallException(ResultCodeEnum.FETCH_USERINFO_ERROR);
        // 根据用户id 订单id 订单状态（待收货）查询订单信息
        OrderInfo orderInfo = this.getOrderInfoOfCondition(userInfo, orderId, OrderStatusEnum.WAIT_TAKE_GOODS.getStatus());
        // 查询结果为空，参数不正确
        if (orderInfo == null) throw new TmallException(ResultCodeEnum.PARAM_ERROR);

        // 更新订单信息
        orderInfo.setOrderStatus(OrderStatusEnum.WAIT_REVIEW.getStatus());
        orderInfo.setConfirmDate(new Date());
        baseMapper.updateById(orderInfo);
    }
}
