package com.study.tmall.product.client;

import com.study.tmall.model.product.ProductInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-11 16:45
 * Versions:1.0.0
 * Description:
 */
@Component
@FeignClient(value = "service-product")
public interface ProductInfoFeignClient {
    // 根据id查询商品（内部调用）
    @PostMapping("/admin/product/productInfo/inner/list")
    List<ProductInfo> listProductInfoById(@RequestBody List<String> idList);
}
