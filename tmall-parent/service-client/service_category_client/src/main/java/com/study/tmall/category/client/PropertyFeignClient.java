package com.study.tmall.category.client;

import com.study.tmall.model.category.Property;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-11 15:51
 * Versions:1.0.0
 * Description:
 */
@Component
@FeignClient(value = "service-category")
public interface PropertyFeignClient {
    // 内部调用，显示对应分类下的所有属性
    @GetMapping("/admin/category/property/inner/show/{categoryId}")
    List<Property> showByCid(@PathVariable("categoryId") String categoryId);
}
