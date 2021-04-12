package com.study.tmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.tmall.category.client.PropertyFeignClient;
import com.study.tmall.exception.TmallException;
import com.study.tmall.model.category.Property;
import com.study.tmall.model.product.ProductInfo;
import com.study.tmall.model.product.PropertyValue;
import com.study.tmall.product.mapper.ProductInfoMapper;
import com.study.tmall.product.service.ProductImageService;
import com.study.tmall.product.service.ProductInfoService;
import com.study.tmall.product.service.PropertyValueService;
import com.study.tmall.result.ResultCodeEnum;
import com.study.tmall.vo.product.ProductQueryVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-10 16:24
 * Versions:1.0.0
 * Description:
 */
@Service
public class ProductInfoServiceImpl extends ServiceImpl<ProductInfoMapper, ProductInfo> implements ProductInfoService {
    @Resource
    private ProductImageService productImageService;
    @Resource
    private PropertyFeignClient propertyFeignClient;
    @Resource
    private PropertyValueService propertyValueService;

    /**
     * 批量删除商品
     * @param idList
     */
    @Override
    public void batchRemove(List<String> idList) {
        idList.stream().forEach(item -> {
            this.removeProductById(item);
        });
    }

    /**
     * 分页条件显示商品
     * @param page
     * @param productQueryVo
     * @return
     */
    @Override
    public IPage<ProductInfo> findPageProductInfo(Page<ProductInfo> page, ProductQueryVo productQueryVo) {
        QueryWrapper<ProductInfo> wrapper = new QueryWrapper<>();
        String categoryId = productQueryVo.getCategoryId(); // 分类id
        String keyword = productQueryVo.getKeyword(); // 商品名 或 商品小标题
        BigDecimal lowPrice = productQueryVo.getLowPrice(); // 最低价格
        BigDecimal highPrice = productQueryVo.getHighPrice(); // 最高价格
        Date createTimeBegin = productQueryVo.getCreateTimeBegin(); // 创建时间区间
        Date createTimeEnd = productQueryVo.getCreateTimeEnd(); // 创建时间区间

        if (StringUtils.isEmpty(categoryId)){ // 不能让分类id为空，分类id为空则抛出异常
            throw new TmallException(ResultCodeEnum.PARAM_ERROR);
        }
        wrapper.eq("category_id", categoryId);
        if (!StringUtils.isEmpty(keyword)){
            //wrapper.like("promote_price", lowPrice);
            // 这种写法相当于    有括号
            // select * from user where status = '1' and (name like '%张%' or phone like '%张%')
            wrapper.and(qw->qw.like("name", keyword).or().like("sub_title", keyword));
            // 而下面这个相当于   无括号
            // select * from user where status = '1' and name like '%张%' or phone like '%张%'
            //wrapper.like("name", keyword).or().like("sub_title", keyword);
        }
        if (lowPrice != null){
            wrapper.ge("promote_price", lowPrice);
        }
        if (highPrice != null){
            wrapper.le("promote_price", highPrice);
        }
        if (createTimeBegin != null){
            wrapper.ge("create_time", createTimeBegin);
        }
        if (createTimeEnd != null){
            wrapper.le("create_time", createTimeEnd);
        }

        IPage<ProductInfo> productInfoIPage = baseMapper.selectPage(page, wrapper);
        productInfoIPage.getRecords().stream().forEach(item -> {
            // 把第一张缩略图装进去
            this.packImage(item);
        });
        return productInfoIPage;
    }

    /**
     * 添加商品
     * @param productInfo
     */
    @Override
    public void saveProduct(ProductInfo productInfo) {
        // 添加商品
        baseMapper.insert(productInfo);

        // 根据分类属性创建商品属性值，默认值空字符串
        // 远程调用category查询分类属性
        List<Property> propertyList = propertyFeignClient.showByCid(productInfo.getCategoryId());
        propertyList.stream().forEach(item -> {
            // 调用 PropertyValueService 保存商品属性值
            PropertyValue propertyValue = new PropertyValue();
            propertyValue.setPropertyId(item.getId());
            propertyValue.setProductId(productInfo.getId());
            propertyValueService.save(propertyValue);
        });
    }

    /**
     * 删除商品
     * @param id
     */
    @Override
    public void removeProductById(String id) {
        // 如果没有这个商品则抛出参数异常
        ProductInfo productInfo = baseMapper.selectById(id);
        if (productInfo == null) {
            throw new TmallException(ResultCodeEnum.PARAM_ERROR);
        }

        // 删除商品下的属性值
        propertyValueService.removeByPid(id);

        // 删除商品下的图片
        productImageService.removeImageByPid(id);

        // 不能删除商品下的评论，因为评论属于用户
        // 不能删除商品对应的订单，因为订单属于用户

        // 删除商品
        baseMapper.deleteById(id);
    }

    /**
     * 根据id查询商品（内部调用）
     * @param idList
     * @return
     */
    @Override
    public List<ProductInfo> listProductInfoById(List<String> idList) {
        // 如果idList没有就返回空
        if (idList == null || idList.size() == 0) return null;
        List<ProductInfo> list = new ArrayList<>();
        // 遍历封装
        idList.stream().forEach(item -> {
            ProductInfo productInfo = baseMapper.selectById(item);
            if (productInfo != null) {
                this.packImage(productInfo); // 把图片搞进去
            }
            // 即使查询出来为空也装入集合，保证数据不错位
            list.add(productInfo);
        });
        return list;
    }

    // 把第一张缩略图装进去
    private ProductInfo packImage(ProductInfo productInfo) {
        // 从数据库查询产品第一张缩略图的url
        String imageUrl = productImageService.getFirstThumbnailImage(productInfo.getId());
        productInfo.getParams().put("imageUrl", imageUrl);
        return productInfo;
    }
}
