package com.study.tmall.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.tmall.model.product.ProductInfo;
import com.study.tmall.product.mapper.ProductInfoMapper;
import com.study.tmall.product.service.ProductInfoService;
import org.springframework.stereotype.Service;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-10 16:24
 * Versions:1.0.0
 * Description:
 */
@Service
public class ProductInfoServiceImpl extends ServiceImpl<ProductInfoMapper, ProductInfo> implements ProductInfoService {
}
