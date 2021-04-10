package com.study.tmall.product.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.study.tmall.model.product.ProductInfo;
import com.study.tmall.product.service.ProductInfoService;
import com.study.tmall.result.Result;
import com.study.tmall.vo.product.ProductQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-10 16:27
 * Versions:1.0.0
 * Description:
 */
@Api(tags = "后台商品管理")
@RestController
@RequestMapping("/admin/product/productInfo")
public class ProductInfoController {
    @Resource
    private ProductInfoService productInfoService;

    // 分页条件显示商品
    @ApiOperation(value = "分页条件显示商品")
    @GetMapping("/list/{current}/{limit}")
    public Result findPageProductInfo(
            @ApiParam(name = "current", value = "起始页", required = true)
            @PathVariable Integer current,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Integer limit,

            @ApiParam(name = "productQueryVo", value = "条件查询商品vo")
            ProductQueryVo productQueryVo){

        Page<ProductInfo> page = new Page<>(current, limit);
        IPage<ProductInfo> pageModule = productInfoService.findPageProductInfo(page, productQueryVo);
        return Result.ok(pageModule);
    }

    // 批量删除商品
    @ApiOperation(value = "批量删除商品")
    @DeleteMapping("/batchRemove")
    public Result batchRemove(
            @ApiParam(name = "idList", value = "商品id集合", required = true)
            @RequestBody List<String> idList){

        productInfoService.batchRemove(idList);
        return Result.ok();
    }

    // 删除商品
    @ApiOperation(value = "删除商品")
    @DeleteMapping("/{id}")
    public Result remove(
            @ApiParam(name = "id", value = "商品id", required = true)
            @PathVariable String id){

        productInfoService.removeById(id);
        return Result.ok();
    }

    // 添加商品
    @ApiOperation(value = "添加商品")
    @PostMapping("/save")
    public Result save(
            @ApiParam(name = "productInfo", value = "商品实体类", required = true)
            @RequestBody ProductInfo productInfo){

        productInfoService.save(productInfo);
        return Result.ok();
    }

    // 编辑商品
    @ApiOperation(value = "编辑商品")
    @PutMapping("/update")
    public Result update (
            @ApiParam(name = "productInfo", value = "商品实体类", required = true)
            @RequestBody ProductInfo productInfo){

        productInfoService.updateById(productInfo);
        return Result.ok();
    }
}
