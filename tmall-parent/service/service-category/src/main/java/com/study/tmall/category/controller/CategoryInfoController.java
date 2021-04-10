package com.study.tmall.category.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.study.tmall.category.service.CategoryInfoService;
import com.study.tmall.model.category.CategoryInfo;
import com.study.tmall.result.Result;
import com.study.tmall.vo.category.CategoryQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-09 19:57
 * Versions:1.0.0
 * Description:
 */
@Api(tags = "后台分类管理")
@RestController
@RequestMapping("/admin/category/categoryInfo")
public class CategoryInfoController {
    @Resource
    private CategoryInfoService categoryInfoService;

    // 分页条件显示分类
    @ApiOperation(value = "分页条件显示分类")
    @GetMapping("/list/{current}/{limit}")
    public Result findPageCategoryInfo(
            @ApiParam(name = "current", value = "起始页", required = true)
            @PathVariable Integer current,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Integer limit,

            @ApiParam(name = "categoryQueryVo", value = "前端分类查询对象")
            CategoryQueryVo categoryQueryVo){

        Page<CategoryInfo> page = new Page<>(current, limit);
        IPage<CategoryInfo> pageModule = categoryInfoService.findPageCategoryInfo(page, categoryQueryVo);
        return Result.ok(pageModule);
    }

    // 添加分类
    @ApiOperation(value = "添加分类")
    @PostMapping("/save/{name}")
    public Result save(
            @ApiParam(name = "name", value = "分类名称", required = true)
            @PathVariable String name,

            @ApiParam(name = "file", value = "分类图片")
            MultipartFile file){

        // 设置分类参数
        CategoryInfo categoryInfo = new CategoryInfo();
        categoryInfo.setName(name);
        // 写入数据库中
        categoryInfoService.saveCategory(categoryInfo, file);
        return Result.ok();
    }

    // 删除分类
    @ApiOperation(value = "删除分类")
    @DeleteMapping("/{id}")
    public Result remove(
            @ApiParam(name = "id", value = "分类id", required = true)
            @PathVariable String id){

        categoryInfoService.removeCategoryById(id);
        return Result.ok();
    }

    // 批量删除分类
    @ApiOperation(value = "批量删除分类")
    @DeleteMapping("/batchRemove")
    public Result batchRemove(
            @ApiParam(name = "idList", value = "分类id集合", required = true)
            @RequestBody List<String> idList){
        categoryInfoService.batchRemoveCategory(idList);
        return Result.ok();
    }

    // 编辑分类
    @ApiOperation(value = "编辑分类")
    @PutMapping("/update/{id}/{name}")
    public Result update(
            @ApiParam(name = "id", value = "分类id", required = true)
            @PathVariable String id,

            @ApiParam(name = "name", value = "分类名称")
            @PathVariable String name,

            @ApiParam(name = "file", value = "分类图片")
            MultipartFile file){

        // 设置参数
        CategoryInfo categoryInfo = new CategoryInfo();
        categoryInfo.setId(id);
        categoryInfo.setName(name);
        // 更新分类
        categoryInfoService.updateCategoryById(categoryInfo, file);

        return Result.ok();
    }
}
