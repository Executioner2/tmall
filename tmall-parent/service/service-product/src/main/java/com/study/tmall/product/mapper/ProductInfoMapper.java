package com.study.tmall.product.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.study.tmall.model.product.ProductInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-10 16:22
 * Versions:1.0.0
 * Description:
 */
public interface ProductInfoMapper extends BaseMapper<ProductInfo> {
    // 传入分类id集合，返回商品小标题
    List<ProductInfo> listProductInfoSubTitle(List<String> idList);

    // 查询出所有商品以及按指定方式排序，因为要查询出评价数所以自己写多表查询语句
    // TODO ${ew.customSqlSegment} 有sql注入的风险，后续改成手写条件
    IPage<ProductInfo> selectProductInfoPageOrderBy(IPage<ProductInfo> page, @Param(Constants.WRAPPER) Wrapper<ProductInfo> queryWrapper);
}
