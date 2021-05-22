package com.study.tmall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.study.tmall.model.product.Review;
import com.study.tmall.vo.front.ProductReviewReturnVo;
import org.apache.ibatis.annotations.Param;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-25 23:43
 * Versions:1.0.0
 * Description:
 */
public interface ReviewMapper extends BaseMapper<Review> {
    // 分页查询商品评价
    IPage<ProductReviewReturnVo> selectProductReviewByProductId(IPage<ProductReviewReturnVo> page, @Param("productId") String productId);
}
