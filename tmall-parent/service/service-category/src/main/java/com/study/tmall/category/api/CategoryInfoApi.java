package com.study.tmall.category.api;

import com.study.tmall.category.service.CategoryInfoService;
import com.study.tmall.model.category.CategoryInfo;
import com.study.tmall.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-23 18:05
 * Versions:1.0.0
 * Description:
 */
@Api(tags = "前台分类接口")
@RestController
@RequestMapping("/api/category/categoryInfo")
public class CategoryInfoApi {
    @Resource
    private CategoryInfoService categoryInfoService;

    // 首页显示分类列表和热销商品
    @ApiOperation(value = "首页显示分类列表和热销商品")
    @GetMapping("/list")
    public Result listCategoryInfo() {
        List<CategoryInfo> list = categoryInfoService.homeListCategoryInfo();
        return Result.ok(list);
    }

    // 根据id查询出分类
    @ApiOperation(value = "根据id查询出分类")
    @GetMapping("/{id}")
    public Result getCategoryInfo(
            @ApiParam(name = "id", value = "分类id", required = true)
            @PathVariable String id) {
        CategoryInfo categoryInfo = categoryInfoService.getById(id);
        return Result.ok(categoryInfo);
    }

}
