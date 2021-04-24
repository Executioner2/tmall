package com.study.tmall.product.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.study.tmall.model.product.ProductInfo;
import com.study.tmall.product.mapper.ProductInfoMapper;
import com.study.tmall.vo.product.ProductInfoEeVo;
import org.springframework.beans.BeanUtils;

import java.util.Map;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-24 21:41
 * Versions:1.0.0
 * Description:
 */
public class ProductInfoListener extends AnalysisEventListener<ProductInfoEeVo> {
    private ProductInfoMapper productInfoMapper; // 商品信息的mapper


    // 有参构造，以便于在invoke方法中添加向数据库添加数据
    public ProductInfoListener (ProductInfoMapper productInfoMapper) {
        this.productInfoMapper = productInfoMapper;
    }


    // 一行一行的读，从第二行开始读取，第一行是表头
    @Override
    public void invoke(ProductInfoEeVo productInfoEeVo, AnalysisContext analysisContext) {
        ProductInfo productInfo = new ProductInfo();
        BeanUtils.copyProperties(productInfoEeVo, productInfo);
        productInfoMapper.insert(productInfo); // 一行一行的向数据库添加商品信息
    }

    // 读取表头
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
    }

    // 读取之后执行
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
