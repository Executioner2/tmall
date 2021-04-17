package com.study.tmall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.study.tmall.model.product.PropertyValue;
import com.study.tmall.vo.product.PropertyAndValueVo;

import java.util.List;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-10 16:26
 * Versions:1.0.0
 * Description:
 */
public interface PropertyValueService extends IService<PropertyValue> {
    // 显示商品属性值
    List<PropertyAndValueVo> show(String productId);

    // 根据商品id删除商品下的属性值
    void removeByPid(String productId);

    // 异步更新商品属性值
    void updatePropertyValueById(PropertyAndValueVo propertyAndValueVo);
}
