package com.study.tmall.category.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.tmall.category.mapper.PropertyMapper;
import com.study.tmall.category.service.PropertyService;
import com.study.tmall.exception.TmallException;
import com.study.tmall.model.category.Property;
import com.study.tmall.result.ResultCodeEnum;
import com.study.tmall.vo.category.PropertyQueryVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-09 19:57
 * Versions:1.0.0
 * Description:
 */
@Service
public class PropertyServiceImpl extends ServiceImpl<PropertyMapper, Property> implements PropertyService {


    /**
     * 批量删除属性
     * @param idList
     */
    @Override
    public void batchRemove(List<String> idList) {
//        idList.stream().forEach(item -> {
//            baseMapper.deleteById(item);
//        });
        // lambda表达式引用方法
        idList.stream().forEach(baseMapper::deleteById);
    }

    /**
     * 分页条件显示属性
     * @param page
     * @param propertyQueryVo
     * @return
     */
    @Override
    public IPage<Property> findPageProperty(Page<Property> page, PropertyQueryVo propertyQueryVo) {
        QueryWrapper<Property> wrapper = new QueryWrapper<>();
        String categoryId = propertyQueryVo.getCategoryId();
        String name = propertyQueryVo.getName();
        Date createTimeBegin = propertyQueryVo.getCreateTimeBegin();
        Date createTimeEnd = propertyQueryVo.getCreateTimeEnd();

        if (StringUtils.isEmpty(categoryId)){
            throw new TmallException(ResultCodeEnum.PARAM_ERROR);
        }
        wrapper.eq("category_id", categoryId);
        if (!StringUtils.isEmpty(name)){
            wrapper.like("name", name);
        }
        if (createTimeBegin != null){
            wrapper.ge("create_time", createTimeBegin);
        }
        if (createTimeEnd != null) {
            wrapper.le("create_time", createTimeEnd);
        }

        IPage<Property> propertyPage = baseMapper.selectPage(page, wrapper);

        return propertyPage;
    }

    /**
     * 根据分类id显示所有属性
     * @param categoryId
     * @return
     */
    @Override
    public List<Property> showByCid(String categoryId) {
        QueryWrapper<Property> wrapper = new QueryWrapper<>();
        wrapper.eq("category_id", categoryId);
        return baseMapper.selectList(wrapper);
    }

    /**
     * 删除分类下的属性
     * @param cid
     */
    @Override
    public void removeByCid(String cid) {
        QueryWrapper<Property> wrapper = new QueryWrapper<>();
        wrapper.eq("category_id", cid);
        baseMapper.delete(wrapper);
    }
}
