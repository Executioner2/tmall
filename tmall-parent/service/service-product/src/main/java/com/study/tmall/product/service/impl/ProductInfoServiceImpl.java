package com.study.tmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.tmall.exception.TmallException;
import com.study.tmall.model.product.ProductInfo;
import com.study.tmall.product.mapper.ProductInfoMapper;
import com.study.tmall.product.service.ProductImageService;
import com.study.tmall.product.service.ProductInfoService;
import com.study.tmall.result.ResultCodeEnum;
import com.study.tmall.vo.product.ProductQueryVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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

    /**
     * 批量删除商品
     * @param idList
     */
    @Override
    public void batchRemove(List<String> idList) {
        idList.stream().forEach(item -> {
            baseMapper.deleteById(item);
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

    // 把第一张缩略图装进去
    private ProductInfo packImage(ProductInfo productInfo) {
        // 从数据库查询产品第一张缩略图的url
        String imageUrl = productImageService.getFirstThumbnailImage(productInfo.getId());
        productInfo.getParams().put("imageUrl", imageUrl);
        return productInfo;
    }
}
