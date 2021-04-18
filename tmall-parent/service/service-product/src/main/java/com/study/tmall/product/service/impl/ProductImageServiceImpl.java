package com.study.tmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.tmall.enums.ImageTypeEnum;
import com.study.tmall.exception.TmallException;
import com.study.tmall.model.product.ProductImage;
import com.study.tmall.model.product.ProductInfo;
import com.study.tmall.product.mapper.ProductImageMapper;
import com.study.tmall.product.service.ProductImageService;
import com.study.tmall.product.service.ProductInfoService;
import com.study.tmall.result.ResultCodeEnum;
import com.study.tmall.util.FastDFSUtil;
import com.study.tmall.util.ImageUtil;
import com.study.tmall.vo.product.ProductImageReturnVo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-10 16:25
 * Versions:1.0.0
 * Description:
 */
@Service
public class ProductImageServiceImpl extends ServiceImpl<ProductImageMapper, ProductImage> implements ProductImageService {
    @Resource
    private ProductInfoService productInfoService;

    /**
     * 批量上传图片
     * @param productImage
     * @param files
     */
    @Override
    public void batchUploadImage(ProductImage productImage, MultipartFile[] files) {
        // 先查询数据库中是否有这个商品
        try {
            ProductInfo productInfo = productInfoService.getById(productImage.getProductId());
            if (files == null || productInfo == null){
                throw new TmallException(ResultCodeEnum.PARAM_ERROR);
            }
            // 上传图片
            for (MultipartFile file : files) {
                ProductImage newProductImage = new ProductImage(); // 这里重新创建新的对象，是为了生成新的id
                newProductImage.setProductId(productImage.getProductId());
                newProductImage.setType(productImage.getType());

                String[] upload = FastDFSUtil.upload(file.getBytes(), ImageUtil.getFileExtName(file));
                newProductImage.setUrl(ImageUtil.compoundUrl(upload));

                // 保存到数据库
                baseMapper.insert(newProductImage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示商品图片
     * @param productId
     * @return
     */
    @Override
    public Map<String, List<ProductImageReturnVo>> showByProductId(String productId) {
        // 根据商品id查询出对应的所有商品图片
        QueryWrapper<ProductImage> wrapper = new QueryWrapper<>();
        wrapper.eq("product_id", productId);

        List<ProductImage> productImages = baseMapper.selectList(wrapper);
        // 让图片list集合 按 type 进行分组
        Map<Integer, List<ProductImage>> collect = productImages.stream().collect(Collectors.groupingBy(ProductImage::getType));

        // 对分组结果进行简化包装，前端只需要productImage 的 id 和 url，type可以根据map得到
        Map<String, List<ProductImageReturnVo>> map = new HashMap<>();
        Iterator<Integer> iterator = collect.keySet().iterator();
        while (iterator.hasNext()){
            Integer type = iterator.next();
            List<ProductImageReturnVo> list = new ArrayList<>();
            for (ProductImage pi : collect.get(type)) {
                ProductImageReturnVo vo = new ProductImageReturnVo();
                vo.setId(pi.getId());
                vo.setUrl(pi.getUrl());
                list.add(vo);
            }
            map.put(ImageTypeEnum.getNameByType(type), list);
        }

        return map;
    }

    /**
     * 删除图片
     * @param id
     */
    @Override
    public void removeImageById(String id) {
        // 查询数据库中是否存在该数据
        ProductImage productImage = baseMapper.selectById(id);
        if (productImage == null) {
            throw new TmallException(ResultCodeEnum.PARAM_ERROR);
        }

        // 删除fastDFS中的图片
        String[] splitUrl = ImageUtil.splitUrl(productImage.getUrl());
        FastDFSUtil.delete(splitUrl[1], splitUrl[2]);

        // 逻辑删除数据库中的记录
        baseMapper.deleteById(productImage);
    }

    /**
     * 批量删除图片
     * @param idList
     */
    @Override
    public void batchRemove(List<String> idList) {
        idList.stream().forEach(item -> {
            this.removeImageById(item);
        });
    }

    /**
     * 从数据库查询产品第一张缩略图的url
     * @param id
     * @return
     */
    @Override
    public String getFirstThumbnailImage(String id) {
        ProductImage firstThumbnailImage = baseMapper.getFirstThumbnailImage(id);
        if (firstThumbnailImage == null){
            return "";
        }
        return firstThumbnailImage.getUrl();
    }

    /**
     * 根据商品id删除图片
     * @param pid
     */
    @Override
    public void removeImageByPid(String pid) {
        // 查询数据库中是否存在数据
        QueryWrapper<ProductImage> wrapper = new QueryWrapper<>();
        wrapper.eq("product_id", pid);
        List<ProductImage> productImages = baseMapper.selectList(wrapper);

        if (productImages != null) { // 如果不为空则开始删除
            productImages.stream().forEach(item -> {
                // 删除fastDFS中的图片
                String[] splitUrl = ImageUtil.splitUrl(item.getUrl());
                FastDFSUtil.delete(splitUrl[1], splitUrl[2]);
            });
            // 逻辑删除数据库中的记录
            baseMapper.delete(wrapper);
        }
    }
}