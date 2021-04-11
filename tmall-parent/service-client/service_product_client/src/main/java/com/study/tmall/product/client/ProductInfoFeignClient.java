package com.study.tmall.product.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

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
}
