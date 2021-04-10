package com.study.tmall.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.study.tmall.model.product.ProductInfo;
import com.study.tmall.vo.product.ProductQueryVo;

import java.util.List;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-10 16:24
 * Versions:1.0.0
 * Description:
 */
public interface ProductInfoService extends IService<ProductInfo> {
    // 批量删除商品
    void batchRemove(List<String> idList);

    // 分页条件显示商品
    IPage<ProductInfo> findPageProductInfo(Page<ProductInfo> page, ProductQueryVo productQueryVo);
}
