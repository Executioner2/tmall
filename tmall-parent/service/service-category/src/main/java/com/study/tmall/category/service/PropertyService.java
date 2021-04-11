package com.study.tmall.category.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.study.tmall.model.category.Property;
import com.study.tmall.vo.category.PropertyQueryVo;

import java.util.List;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-09 19:57
 * Versions:1.0.0
 * Description:
 */
public interface PropertyService extends IService<Property> {
    // 批量删除属性
    void batchRemove(List<String> idList);

    // 分页条件显示属性
    IPage<Property> findPageProperty(Page<Property> page, PropertyQueryVo propertyQueryVo);

    // 根据分类id显示所有属性
    List<Property> showByCid(String categoryId);

    // 删除分类下的属性
    void removeByCid(String cid);
}
