package com.study.tmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.tmall.category.client.PropertyFeignClient;
import com.study.tmall.exception.TmallException;
import com.study.tmall.model.category.Property;
import com.study.tmall.model.product.ProductInfo;
import com.study.tmall.model.product.PropertyValue;
import com.study.tmall.product.mapper.PropertyValueMapper;
import com.study.tmall.product.service.ProductInfoService;
import com.study.tmall.product.service.PropertyValueService;
import com.study.tmall.result.ResultCodeEnum;
import com.study.tmall.vo.product.PropertyAndValueVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-10 16:26
 * Versions:1.0.0
 * Description:
 */
@Service
public class PropertyValueServiceImpl extends ServiceImpl<PropertyValueMapper, PropertyValue> implements PropertyValueService {
    @Resource
    private ProductInfoService productInfoService;
    @Resource
    private PropertyFeignClient propertyFeignClient;

    /**
     * 显示商品属性值
     * @param productId
     * @return
     */
    @Override
    public List<PropertyAndValueVo> show(String productId) {
        // 查询出categoryId
        ProductInfo productInfo = productInfoService.getById(productId);
        if (productInfo == null) {
            throw new TmallException(ResultCodeEnum.PARAM_ERROR);
        }

        // 用openFeign查询属性
        List<Property> propertyList = propertyFeignClient.showByCid(productInfo.getCategoryId());
        if (propertyList == null) {
            throw new TmallException(ResultCodeEnum.PROPERTY_NULL); // 未录入属性
        }

        // 查询出产品对应的propertyValue
        QueryWrapper<PropertyValue> wrapper = new QueryWrapper<>();
        wrapper.eq("product_id", productId);
        List<PropertyValue> propertyValues = baseMapper.selectList(wrapper);

        // 封装成List<PropertyAndValueVo>
        List<PropertyAndValueVo> list = new ArrayList<>();
        propertyList.stream().forEach(item -> {
            list.add(this.packPropertyValue(propertyValues, item));
        });

        return list;
    }

    /**
     * 根据商品id删除商品下的属性值
     * @param productId
     */
    @Override
    public void removeByPid(String productId) {
        QueryWrapper<PropertyValue> wrapper = new QueryWrapper<>();
        wrapper.eq("product_id", productId);
        baseMapper.delete(wrapper);
    }

    /**
     * 异步更新商品属性值
     * @param propertyAndValueVo
     */
    @Override
    public void updatePropertyValueById(PropertyAndValueVo propertyAndValueVo) {
        String value = propertyAndValueVo.getPropertyValue();
        String id = propertyAndValueVo.getPropertyValueId();
        PropertyValue propertyValue = new PropertyValue();
        propertyValue.setId(id);
        propertyValue.setValue(value);
        baseMapper.updateById(propertyValue);
    }

    /**
     * 封装属性名和属性值
     * @param propertyValues
     * @param property
     * @return
     */
    private PropertyAndValueVo packPropertyValue(List<PropertyValue> propertyValues, Property property) {
        PropertyAndValueVo vo = new PropertyAndValueVo();
        for (int i = 0; i < propertyValues.size(); i++) {
            PropertyValue propertyValue = propertyValues.get(i);
            if (property.getId().equals(propertyValue.getPropertyId())) {
                vo.setPropertyName(property.getName());
                vo.setPropertyValue(propertyValue.getValue());
                vo.setPropertyValueId(propertyValue.getId());
            }
        }
        return vo;
    }
}
