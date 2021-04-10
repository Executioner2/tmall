package com.study.tmall.category.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.study.tmall.category.service.PropertyService;
import com.study.tmall.exception.TmallException;
import com.study.tmall.model.category.Property;
import com.study.tmall.result.Result;
import com.study.tmall.result.ResultCodeEnum;
import com.study.tmall.vo.category.PropertyQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-09 19:59
 * Versions:1.0.0
 * Description:
 */
@Api(tags = "后台属性管理")
@RestController
@RequestMapping("/admin/category/property")
public class PropertyController {
    @Resource
    private PropertyService propertyService;

    // 分页条件显示属性
    @ApiOperation(value = "分页条件显示属性")
    @GetMapping("/list/{current}/{limit}")
    public Result findPageProperty(
            @ApiParam(name = "current", value = "起始页", required = true)
            @PathVariable Integer current,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Integer limit,

            @ApiParam(name = "propertyQueryVo", value = "条件查询vo")
            PropertyQueryVo propertyQueryVo){

        Page<Property> page = new Page<>(current, limit);
        IPage<Property> pageModule = propertyService.findPageProperty(page, propertyQueryVo);
        return Result.ok(pageModule);
    }

    // 批量删除属性
    @ApiOperation(value = "批量删除属性")
    @DeleteMapping("/batchRemove")
    public Result batchRemove(
            @ApiParam(name = "idList", value = "要批量删除属性的id", required = true)
            @RequestBody List<String> idList){

        propertyService.batchRemove(idList);
        return Result.ok();
    }

    // 删除属性
    @ApiOperation(value = "删除属性")
    @DeleteMapping("/{id}")
    public Result remove(
            @ApiParam(name = "id", value = "属性id", required = true)
            @PathVariable String id){

        propertyService.removeById(id);
        return Result.ok();
    }


    // 添加属性
    @ApiOperation(value = "添加属性")
    @PostMapping("/save")
    public Result save(
            @ApiParam(name = "property", value = "属性对象", required = true)
            @RequestBody Property property){

        propertyService.save(property);
        return Result.ok();
    }

    // 编辑属性
    @ApiOperation(value = "编辑属性")
    @PutMapping("/update")
    public Result update(
            @ApiParam(name = "property", value = "属性对象", required = true)
            @RequestBody Property property){

        if (property == null || StringUtils.isEmpty(property.getName())){
            throw new TmallException(ResultCodeEnum.PARAM_ERROR);
        }
        propertyService.updateById(property);
        return Result.ok();
    }
}
