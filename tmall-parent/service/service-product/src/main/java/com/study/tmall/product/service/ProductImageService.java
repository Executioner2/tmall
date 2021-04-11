package com.study.tmall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.study.tmall.model.product.ProductImage;
import com.study.tmall.vo.product.ProductImageReturnVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-10 16:25
 * Versions:1.0.0
 * Description:
 */
public interface ProductImageService extends IService<ProductImage> {
    // 批量上传图片
    void batchUploadImage(String productId, Integer type, MultipartFile[] files);

    // 显示商品图片
    Map<String, List<ProductImageReturnVo>> showByProductId(String productId);

    // 删除图片
    void removeImageById(String id);

    // 批量删除图片
    void batchRemove(List<String> idList);

    // 从数据库查询产品第一张缩略图的url
    String getFirstThumbnailImage(String id);

    // 根据商品id删除图片
    void removeImageByPid(String pid);
}
