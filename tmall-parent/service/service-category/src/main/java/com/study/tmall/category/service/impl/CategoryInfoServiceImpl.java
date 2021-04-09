package com.study.tmall.category.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.tmall.category.mapper.CategoryInfoMapper;
import com.study.tmall.category.service.CategoryInfoService;
import com.study.tmall.model.category.CategoryInfo;
import com.study.tmall.vo.category.CategoryQueryVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-09 19:56
 * Versions:1.0.0
 * Description:
 */
@Service
public class CategoryInfoServiceImpl extends ServiceImpl<CategoryInfoMapper, CategoryInfo> implements CategoryInfoService {

    /**
     * 分页条件显示分类
     * @param page
     * @param categoryQueryVo
     * @return
     */
    @Override
    public IPage<CategoryInfo> findPageCategoryInfo(Page<CategoryInfo> page, CategoryQueryVo categoryQueryVo) {
        QueryWrapper<CategoryInfo> wrapper = new QueryWrapper<>();
        String name = categoryQueryVo.getName();
        Date createTimeBegin = categoryQueryVo.getCreateTimeBegin();
        Date createTimeEnd = categoryQueryVo.getCreateTimeEnd();

        // 如果查询名称为空，则模糊匹配分类名称
        if (!StringUtils.isEmpty(name)){
            wrapper.like("name", name);
        }
        // 如果开始日期不为空
        if (createTimeBegin != null){
            wrapper.ge("create_time", createTimeBegin);
        }
        // 如果结束日期不为空
        if (createTimeBegin != null){
            wrapper.le("create_time", createTimeEnd);
        }

        // 查询并返回结果
        IPage<CategoryInfo> categoryInfoPage = baseMapper.selectPage(page, wrapper);
        return categoryInfoPage;
    }
}
