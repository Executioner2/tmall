package com.study.tmall.vo.product;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-24 21:43
 * Versions:1.0.0
 * Description:
 */
@Data
@ApiModel(description = "excel文档中的商品信息vo类")
public class ProductInfoEeVo {
    @ExcelProperty(value = "id", index = 0)
    private String id;

    @ExcelProperty(value = "分类id", index = 1)
    private String categoryId;

    @ExcelProperty(value = "商品名称", index = 2)
    private String name;

    @ExcelProperty(value = "商品小标题", index = 3)
    private String subTitle;

    @ExcelProperty(value = "商品原价", index = 4)
    private BigDecimal orignalPrice;

    @ExcelProperty(value = "商品实际售价", index = 5)
    private BigDecimal promotePrice;

    @ExcelProperty(value = "库存", index = 6)
    private Long stock;
}
