package com.study.tmall.product.controller;

import com.study.tmall.model.product.ProductImage;
import com.study.tmall.product.service.ProductImageService;
import com.study.tmall.result.Result;
import com.study.tmall.vo.product.ProductImageReturnVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-10 16:28
 * Versions:1.0.0
 * Description:
 */
@Api(tags = "后台商品图片管理")
@RestController
@RequestMapping("/admin/product/productImage")
public class ProductImageController {
    @Resource
    private ProductImageService productImageService;

    // 显示商品图片
    @ApiOperation(value = "显示商品图片")
    @GetMapping("/show/{productId}")
    public Result show(
            @ApiParam(name = "productId", value = "商品id", required = true)
            @PathVariable String productId){

        Map<String, List<ProductImageReturnVo>> map = productImageService.showByProductId(productId);
        return Result.ok(map);
    }

    // 批量上传图片
    @ApiOperation(value = "批量上传图片")
    @PostMapping("/batchUpload/{productId}/{type}")
    public Result batchUploadImage (
            @ApiParam(name = "productId", value = "商品id", required = true)
            @PathVariable String productId,

            @ApiParam(name = "type", value = "图片类型", required = true)
            @PathVariable Integer type,

            @ApiParam(name = "files", value = "批量文件", required = true)
            MultipartFile[] files){

        ProductImage productImage = new ProductImage();
        productImage.setProductId(productId);
        productImage.setType(type);
        productImageService.batchUploadImage(productImage, files);
        return Result.ok();
    }

    // 批量删除图片
    @ApiOperation(value = "批量删除图片")
    @DeleteMapping("/batchRemove")
    public Result batchRemove(
            @ApiParam(name = "idList", value = "批量删除的图片id", required = true)
            @RequestBody List<String> idList){

        productImageService.batchRemove(idList);
        return Result.ok();
    }

    // 删除图片
    @ApiOperation(value = "删除图片")
    @DeleteMapping("/{id}")
    public Result remove(
            @ApiParam(name = "id", value = "图片id", required = true)
            @PathVariable String id){

        productImageService.removeImageById(id);
        return Result.ok();
    }
}
