package com.study.tmall.product.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.study.tmall.model.product.Review;
import com.study.tmall.product.service.ReviewService;
import com.study.tmall.result.Result;
import com.study.tmall.vo.front.ProductReviewReturnVo;
import com.study.tmall.vo.product.ReviewVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-28 20:27
 * Versions:1.0.0
 * Description:
 */
@Api(tags = "前台商品评价接口")
@RestController
@RequestMapping("/api/product/review")
public class ReviewApi {
    @Resource
    private ReviewService reviewService;

    // 根据商品id分页查询商品评价
    @ApiOperation(value = "根据商品id分页查询商品评价")
    @GetMapping("/list/{current}/{limit}/{productId}")
    public Result listReviewByProductId(
            @ApiParam(name = "current", value = "起始页", required = true)
            @PathVariable Long current,

            @ApiParam(name = "limit", value = "每页大小", required = true)
            @PathVariable Long limit,

            @ApiParam(name = "productId", value = "商品id", required = true)
            @PathVariable String productId) {

        Page<ProductReviewReturnVo> page = new Page<>(current, limit);
        IPage pageModule = reviewService.listReviewByProductId(page, productId);
        return Result.ok(pageModule);
    }

    // 添加评价
    @ApiOperation("添加评价")
    @PostMapping("/auth/add")
    public Result addReview(
            @ApiParam(name = "request", value = "request", required = true)
            HttpServletRequest request,

            @ApiParam(name = "reviewVo", value = "商品评价vo类", required = true)
            @RequestBody ReviewVo reviewVo) {

        String token = request.getHeader("token");
        reviewService.addReview(token, reviewVo);
        return Result.ok();
    }

}
