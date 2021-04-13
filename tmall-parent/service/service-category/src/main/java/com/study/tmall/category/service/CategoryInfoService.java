package com.study.tmall.category.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.study.tmall.model.category.CategoryInfo;
import com.study.tmall.vo.category.CategoryQueryVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-09 19:55
 * Versions:1.0.0
 * Description:
 */
public interface CategoryInfoService extends IService<CategoryInfo> {
    // 分页条件显示分类
    IPage<CategoryInfo> findPageCategoryInfo(Page<CategoryInfo> page, CategoryQueryVo categoryQueryVo);

    // 删除分类
    void removeCategoryById(String id);

    // 批量删除分类
    void batchRemoveCategory(List<String> idList);

    // 编辑分类
    void updateCategoryById(CategoryInfo categoryInfo, MultipartFile file);

    // 添加分类
    void saveCategory(CategoryInfo categoryInfo, MultipartFile file);
}