package com.study.tmall.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.study.tmall.model.product.Review;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-25 23:44
 * Versions:1.0.0
 * Description:
 */
public interface ReviewService extends IService<Review> {
    // 根据商品id分页查询商品评价
    IPage listReviewByProductId(Page<Review> page, String productId);
}
