package com.study.tmall.product.controller;

import com.study.tmall.model.product.PropertyValue;
import com.study.tmall.product.service.PropertyValueService;
import com.study.tmall.result.Result;
import com.study.tmall.vo.product.PropertyAndValueVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-10 16:29
 * Versions:1.0.0
 * Description:
 */
@Api(tags = "后台商品属性值管理")
@RestController
@RequestMapping("/admin/product/propertyValue")
public class PropertyValueController {
    @Resource
    private PropertyValueService propertyValueService;

    // 显示商品属性值
    @ApiOperation(value = "显示商品属性值")
    @GetMapping("/show/{productId}")
    public Result show(
            @ApiParam(name = "productId", value = "商品id", required = true)
            @PathVariable String productId){

        List<PropertyAndValueVo> list = propertyValueService.show(productId);
        return Result.ok(list);
    }

    // 异步更新商品属性值
    @ApiOperation(value = "异步更新产品属性值")
    @PutMapping("/update")
    public Result update (
            @ApiParam(name = "propertyAndValueVo", value = "属性和属性值vo", required = true)
            @RequestBody PropertyAndValueVo propertyAndValueVo){

        propertyValueService.updatePropertyValueById(propertyAndValueVo);
        return Result.ok();
    }
}
