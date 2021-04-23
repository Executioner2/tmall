package com.study.tmall.product.client;

import com.study.tmall.model.product.ProductInfo;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

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

    // 返回商品小标题集合（内部调用）
    @PostMapping("/api/product/productInfo/inner/list/simple")
    Map<String, List<ProductInfo>> listProductInfoSubTitle(@RequestBody List<String> idList);

    // 显示每个分类前五个热销商品（内部调用）
    @ApiModelProperty(value = "显示每个分类前五个热销商品（内部调用）")
    @PostMapping("/api/product/productInfo/inner/list/hot")
    Map<String, List<ProductInfo>> listProductInfoHot(@RequestBody List<String> idList);
}
