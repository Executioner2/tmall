package com.study.tmall.category.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.study.tmall.model.category.CategoryInfo;
import com.study.tmall.vo.category.CategoryQueryVo;

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
}
