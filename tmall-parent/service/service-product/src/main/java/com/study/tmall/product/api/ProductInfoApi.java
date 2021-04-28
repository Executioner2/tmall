package com.study.tmall.product.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.study.tmall.model.product.ProductInfo;
import com.study.tmall.product.service.ProductInfoService;
import com.study.tmall.result.Result;
import com.study.tmall.vo.product.ProductInfoFrontQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-23 18:30
 * Versions:1.0.0
 * Description:
 */
@Api(tags = "前台商品信息接口")
@RestController
@RequestMapping("/api/product/productInfo")
public class ProductInfoApi {
    @Resource
    private ProductInfoService productInfoService;

    // 返回商品小标题集合（内部调用）
    @ApiOperation(value = "内部调用，返回商品小标题集合")
    @PostMapping("/inner/list/simple")
    public Map<String, List<ProductInfo>> listProductInfoSubTitle(
            @ApiParam(name = "idList", value = "分类id集合", required = true)
            @RequestBody List<String> idList) {

        return productInfoService.listProductInfoSubTitle(idList);
    }

    // 显示每个分类前五个热销商品（内部调用）
    @ApiOperation(value = "显示每个分类前五个热销商品（内部调用）")
    @PostMapping("/inner/list/hot")
    public Map<String, List<ProductInfo>> listProductInfoHot(
            @ApiParam(name = "idList", value = "分类id集合", required = true)
            @RequestBody List<String> idList) {

        return productInfoService.listProductInfoHot(idList);
    }

    // 分类页，根据分类id和查询条件显示商品
    @ApiOperation(value = "分类页，根据分类id和查询条件显示商品")
    @GetMapping("/list/{current}/{limit}")
    public Result listProductInfo(
            @ApiParam(name = "current", value = "起始页", required = true)
            @PathVariable Long current,

            @ApiParam(name = "limit", value = "每页大小", required = true)
            @PathVariable Long limit,

            @ApiParam(name = "productInfoFrontQueryVo", value = "前台分类页商品查询对象", required = true)
            ProductInfoFrontQueryVo productInfoFrontQueryVo) {

        Page<ProductInfo> page = new Page<>(current, limit);
        IPage<ProductInfo> pageModule = productInfoService.listProductInfo(page, productInfoFrontQueryVo);
        return Result.ok(pageModule);
    }

    // 根据商品id查询出商品信息
    @ApiOperation(value = "根据商品id查询出商品信息")
    @GetMapping("/{id}")
    public Result getProductInfoById(
            @ApiParam(name = "id", value = "商品id", required = true)
            @PathVariable String id) {
        ProductInfo productInfo = productInfoService.getProductInfoById(id);
        return Result.ok(productInfo);
    }

}
