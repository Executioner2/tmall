package com.study.tmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.tmall.model.product.Review;
import com.study.tmall.product.mapper.ReviewMapper;
import com.study.tmall.product.service.ReviewService;
import com.study.tmall.vo.front.ProductReviewReturnVo;
import org.springframework.stereotype.Service;

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

    /**
     * 根据商品id分页查询商品评价
     * @param page
     * @param productId
     * @return
     */
    @Override
    public IPage listReviewByProductId(Page<Review> page, String productId) {
        // 查询条件
        QueryWrapper<Review> wrapper = new QueryWrapper<>();
        wrapper.eq("product_id", productId);
        // 分页查询
        IPage<Review> reviewPage = baseMapper.selectPage(page, wrapper);
        // 设置自己的records
        List<ProductReviewReturnVo> reviewReturnVos = new ArrayList<>();
        reviewPage.getRecords().forEach(item -> {

        });

        return null;
    }
}
