package com.study.tmall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.study.tmall.model.product.ProductInfo;

import java.util.List;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-10 16:22
 * Versions:1.0.0
 * Description:
 */
public interface ProductInfoMapper extends BaseMapper<ProductInfo> {
    // 传入分类id集合，返回商品小标题
    List<ProductInfo> listProductInfoSubTitle(List<String> idList);
}
