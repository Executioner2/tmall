package com.study.tmall.category.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.study.tmall.category.service.CategoryInfoService;
import com.study.tmall.enums.ImageUrlEnum;
import com.study.tmall.model.category.CategoryInfo;
import com.study.tmall.result.Result;
import com.study.tmall.util.FastDFSUtil;
import com.study.tmall.util.ImageUtil;
import com.study.tmall.vo.category.CategoryQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

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
        return Result.ok();
    }

    // 添加分类
    @ApiOperation(value = "添加分类")
    @PostMapping("/save")
    public Result save(
            @ApiParam(name = "categoryInfo", value = "分类实体类", required = true)
            @RequestBody CategoryInfo categoryInfo,

            @ApiParam(name = "file", value = "分类图片")
            MultipartFile file){

        try {
            // 考虑到文件传输体验，所以就在控制层将图片添加到fastDFS文件系统中
            String filename = file.getOriginalFilename(); // 获得文件名
            String fileExtName = "jpg"; // 默认为jpg，防止出错
            if (filename.contains(".")){
                fileExtName = filename.substring(filename.lastIndexOf(".") + 1); // 取得文件拓展名
            }
            // 上传到fastDFS中
            String[] upload = FastDFSUtil.upload(file.getBytes(), fileExtName);
            categoryInfo.setImageUrl(ImageUtil.compoundUrl(upload)); // 设置最终可直接访问的imageUrl地址
            // 写入数据库中
            categoryInfoService.save(categoryInfo);

            return Result.ok();
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail();
        }
    }

}
