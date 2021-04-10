package com.study.tmall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.study.tmall.model.product.ProductImage;
import org.apache.ibatis.annotations.Param;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-10 16:23
 * Versions:1.0.0
 * Description:
 */
public interface ProductImageMapper extends BaseMapper<ProductImage> {
    // 获取第一张缩略图
    ProductImage getFirstThumbnailImage(@Param("pid") String pid);
}
