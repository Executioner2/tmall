package com.study.tmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.tmall.enums.OrderStatusEnum;
import com.study.tmall.enums.ReviewEnum;
import com.study.tmall.exception.TmallException;
import com.study.tmall.model.product.Review;
import com.study.tmall.model.user.UserInfo;
import com.study.tmall.order.client.OrderFeignClient;
import com.study.tmall.product.mapper.ReviewMapper;
import com.study.tmall.product.service.ReviewService;
import com.study.tmall.result.ResultCodeEnum;
import com.study.tmall.user.client.UserFeignClient;
import com.study.tmall.vo.front.ProductReviewReturnVo;
import com.study.tmall.vo.product.ReviewVo;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-25 23:44
 * Versions:1.0.0
 * Description:
 */
@Service
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, Review> implements ReviewService {
    @Resource
    private UserFeignClient userFeignClient;
    @Resource
    private OrderFeignClient orderFeignClient;


    /**
     * 根据商品id分页查询商品评价
     * @param page
     * @param productId
     * @return
     */
    @Override
    @Cacheable(value = "product", keyGenerator = "keyGeneratorPage") // 缓存到redis
    public IPage listReviewByProductId(Page<ProductReviewReturnVo> page, String productId) {
        // 分页查询
        IPage<ProductReviewReturnVo> reviewPage = baseMapper.selectProductReviewByProductId(page, productId);
        // idList，用户id集合，降低远程调用次数
        List<String> idList = new ArrayList<>();
        reviewPage.getRecords().forEach(item -> {
            ProductReviewReturnVo vo = new ProductReviewReturnVo();
            BeanUtils.copyProperties(item, vo);
            idList.add(item.getUserId());
        });

        // 远程调用查询用户信息
        List<UserInfo> userInfoList = userFeignClient.listUserInfoOfInner(idList);
        reviewPage.getRecords().forEach(item -> {
            // TODO 后续给用户提供匿名可选，不匿名则显示全名称
            // 封装匿名用户名称
            this.packUserName(item, userInfoList);
        });

        return reviewPage;
    }

    /**
     * 添加评价
     * @param token
     * @param reviewVo
     */
    @Override
    @CacheEvict(value = "product", allEntries = true) // 清除redis缓存
    public void addReview(String token, ReviewVo reviewVo) {
        // 核实用户信息
        UserInfo userInfo = userFeignClient.getUserInfoByToken(token);
        if (userInfo == null) throw new TmallException(ResultCodeEnum.FETCH_USERINFO_ERROR);

        // 增加评价
        Review review = new Review();
        BeanUtils.copyProperties(reviewVo, review);
        review.setUserId(userInfo.getId());
        baseMapper.insert(review);

        // 更新订单项评价状态
        orderFeignClient.updateReviewStatus(reviewVo.getOrderItemId(), ReviewEnum.REVIEW.getStatus());
    }

    // 封装匿名用户名称
    private void packUserName(ProductReviewReturnVo item, List<UserInfo> userInfoList) {
        for (int i = 0; i < userInfoList.size(); i++) {
            UserInfo userInfo = userInfoList.get(i);
            if (item.getUserId().equals(userInfo.getId())) {
                // 取得用户名，nick_name > name
                StringBuilder name = new StringBuilder();
                if (!StringUtils.isEmpty(userInfo.getNickName())) { // 昵称
                    name.append(userInfo.getNickName());
                } else if(!StringUtils.isEmpty(userInfo.getName())) { // 用户名
                    name.append(userInfo.getName());
                }
                // 把用户名处理为星星
                // 例如：张（*） 张三（张*） 张小三（张*三） 张小小三（张**三）
                if (name.length() <= 1) {
                    name.delete(0, 1);
                    name.append("*");
                } else if(name.length() <= 2) {
                    name.delete(1, 2);
                    name.append("*");
                } else {
                    Integer length = name.length() - 2;
                    for (int j = 1; j <= length; j++) {
                        name.replace(j, j + 1, "*");
                    }
                }
                item.setAnonymity(name.toString());
                break;
            }
        }
    }
}
