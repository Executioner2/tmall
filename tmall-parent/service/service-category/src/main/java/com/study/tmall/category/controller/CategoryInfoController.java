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
import java.util.Map;

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
            @PathVariable Long current,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,

            @ApiParam(name = "categoryQueryVo", value = "前端分类查询对象")
            CategoryQueryVo categoryQueryVo){

        Page<CategoryInfo> page = new Page<>(current, limit);
        IPage<CategoryInfo> pageModule = categoryInfoService.findPageCategoryInfo(page, categoryQueryVo);
        return Result.ok(pageModule);
    }

    // 添加分类
    @ApiOperation(value = "添加分类")
    @PostMapping("/save")
    public Result save(
            @ApiParam(name = "categoryInfo", value = "分类", required = true)
            @RequestBody CategoryInfo categoryInfo){ // 应该有分类名和分类图片的地址

        categoryInfoService.save(categoryInfo);
        return Result.ok();
    }

    // 添加分类图片
    @ApiOperation(value = "添加分类")
    @PostMapping("/saveImage")
    public Result saveImage(
            @ApiParam(name = "file", value = "分类图片")
            MultipartFile file){

        Map<String, String> map = categoryInfoService.saveImage(file);
        return Result.ok(map);
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
    @PutMapping("/update")
    public Result update(
            @ApiParam(name = "categoryInfo", value = "分类信息（包含id和图片地址）", required = true)
            @RequestBody CategoryInfo categoryInfo){

        // 更新分类
        categoryInfoService.updateCategoryById(categoryInfo);
        return Result.ok();
    }

    // 根据分类id获得分类信息（内部调用）
    @ApiOperation(value = "根据分类id获得分类信息（内部调用）")
    @GetMapping("/inner/get/{id}")
    public CategoryInfo getById(
            @ApiParam(name = "id", value = "分类id", required = true)
            @PathVariable String id) {
        return categoryInfoService.getById(id);
    }
}
