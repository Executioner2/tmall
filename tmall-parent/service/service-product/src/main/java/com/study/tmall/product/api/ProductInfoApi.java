package com.study.tmall.product.api;

import com.study.tmall.model.product.ProductInfo;
import com.study.tmall.product.service.ProductInfoService;
import com.study.tmall.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @ApiModelProperty(value = "内部调用，返回商品小标题集合")
    @PostMapping("/inner/list/simple")
    public Map<String, List<ProductInfo>> listProductInfoSubTitle(
            @ApiParam(value = "idList", name = "分类id集合", required = true)
            @RequestBody List<String> idList) {

        return productInfoService.listProductInfoSubTitle(idList);
    }

    // 显示每个分类前五个热销商品（内部调用）
    @ApiModelProperty(value = "显示每个分类前五个热销商品（内部调用）")
    @PostMapping("/inner/list/hot")
    public Map<String, List<ProductInfo>> listProductInfoHot(
            @ApiParam(value = "idList", name = "分类id集合", required = true)
            @RequestBody List<String> idList) {

        return productInfoService.listProductInfoHot(idList);
    }

}
