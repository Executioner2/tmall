package com.study.tmall.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.study.tmall.model.product.ProductInfo;
import com.study.tmall.dto.ProductStock;
import com.study.tmall.vo.product.ProductInfoFrontQueryVo;
import com.study.tmall.vo.product.ProductQueryVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-10 16:24
 * Versions:1.0.0
 * Description:
 */
public interface ProductInfoService extends IService<ProductInfo> {
    // 批量删除商品
    void batchRemove(List<String> idList);

    // 分页条件显示商品
    IPage<ProductInfo> findPageProductInfo(Page<ProductInfo> page, ProductQueryVo productQueryVo);

    // 添加商品
    void saveProduct(ProductInfo productInfo);

    // 删除商品
    void removeProductById(String id);

    // 根据id查询商品（内部调用）
    List<ProductInfo> listProductInfoById(List<String> idList);

    // 返回商品小标题集合  内部调用
    Map<String, List<ProductInfo>> listProductInfoSubTitle(List<String> idList);

    // 显示每个分类前五个热销商品（内部调用）
    Map<String, List<ProductInfo>> listProductInfoHot(List<String> idList);

    // 根据上传的excel文档添加商品数据到数据库
    void importData(MultipartFile file);

    // 把商品信息导出到excel文件中
    void exportDictData(HttpServletResponse response);

    // 分类页，根据分类id和查询条件显示商品
    IPage<ProductInfo> listProductInfo(Page<ProductInfo> page, ProductInfoFrontQueryVo productInfoFrontQueryVo);

    // 根据商品id查询出商品信息
    ProductInfo getProductInfoById(String id);

    // 商品搜索，分页显示
    IPage<ProductInfo> searchProductInfo(Page<ProductInfo> page, String keyword);

    // 更新商品库存
    Long updateProductStock(ProductStock productStock);

    // 批量更新商品库存
    void updateProductStock(List<ProductStock> productStock);

    // 根据id查询商品，取得商品简单信息
    ProductInfo getProductSimpleInfoById(String id);

    // 更新商品月销量，总销量
    void updateSales(Map<String, Integer> paramsMap);
}
