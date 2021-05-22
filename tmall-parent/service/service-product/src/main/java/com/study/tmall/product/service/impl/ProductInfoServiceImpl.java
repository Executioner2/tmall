package com.study.tmall.product.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.tmall.category.client.CategoryClient;
import com.study.tmall.enums.ArithmeticTypeEnum;
import com.study.tmall.enums.ProductSortStatusEnum;
import com.study.tmall.enums.SortTypeEnum;
import com.study.tmall.exception.TmallException;
import com.study.tmall.model.category.Property;
import com.study.tmall.model.product.ProductInfo;
import com.study.tmall.model.product.PropertyValue;
import com.study.tmall.model.product.Review;
import com.study.tmall.product.listener.ProductInfoListener;
import com.study.tmall.product.mapper.ProductInfoMapper;
import com.study.tmall.product.service.ProductImageService;
import com.study.tmall.product.service.ProductInfoService;
import com.study.tmall.product.service.PropertyValueService;
import com.study.tmall.product.service.ReviewService;
import com.study.tmall.result.ResultCodeEnum;
import com.study.tmall.vo.after_end.ProductStockVo;
import com.study.tmall.vo.product.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

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
    private PropertyValueService propertyValueService;
    @Resource
    private CategoryClient categoryClient;
    @Resource
    private ReviewService reviewService;

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
        List<Property> propertyList = categoryClient.showByCid(productInfo.getCategoryId());
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

    /**
     * 返回商品小标题集合  内部调用
     * @param idList
     * @return
     */
    @Override
    public Map<String, List<ProductInfo>> listProductInfoSubTitle(List<String> idList) {
        List<ProductInfo> productInfoList = baseMapper.listProductInfoSubTitle(idList);
        if (productInfoList == null) {
            return null;
        }
        // 对查询出来的结果按categoryId进行分组
        Map<String, List<ProductInfo>> collect = productInfoList.stream().collect(Collectors.groupingBy(ProductInfo::getCategoryId));
        return collect;
    }

    /**
     * 显示每个分类前五个热销商品（内部调用）
     * @param idList
     * @return
     */
    @Override
    public Map<String, List<ProductInfo>> listProductInfoHot(List<String> idList) {
        // 查询五条记录
        Page<ProductInfo> page = new Page<>(1, 5);
        QueryWrapper<ProductInfo> wrapper = new QueryWrapper<>();

        // 放入到map集合中，分类id作为key，查询结果的Records作为value
        Map<String, List<ProductInfo>> map = new HashMap<>();
        for (String str : idList) {
            // 按月销量进行降序排序
            wrapper.eq("category_id", str);
            wrapper.orderByDesc("monthly_sales");
            IPage<ProductInfo> pageModule = baseMapper.selectPage(page, wrapper);
            wrapper.clear(); // 清空条件
            // 对商品数据再进行处理
            pageModule.getRecords().stream().forEach(item -> {
                // 把第一张缩略图封装进去
                this.packImage(item);
                // 把每个商品的名称缩短，方便前端显示（显示商品名称的前25个字符）
                item.setName(item.getName().substring(0, 25));
            });
            map.put(str, pageModule.getRecords());
        }
        return map;
    }

    /**
     * 根据上传的excel文档添加商品数据到数据库
     * @param file
     */
    @Override
    public void importData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), ProductInfoEeVo.class, new ProductInfoListener(baseMapper)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 把商品信息导出到excel文件中
     * @param response
     */
    @Override
    public void exportDictData(HttpServletResponse response) {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        try {
            // 设置文件名
            String fileName = URLEncoder.encode("商品信息表", "utf-8");
            // 参数1：设置头信息以下载方式打开
            // 参数2：文件名
            response.setHeader("Content-disposition", "attachment;filename="+ fileName + ".xlsx");
            // 根据分类id查询商品信息
            QueryWrapper<ProductInfo> wrapper = new QueryWrapper<>();
            List<ProductInfo> productInfoList = baseMapper.selectList(wrapper);
            List<ProductInfoEeVo> productInfoEeVos = new ArrayList<>();
            for (ProductInfo productInfo : productInfoList) {
                ProductInfoEeVo productInfoEeVo = new ProductInfoEeVo();
                BeanUtils.copyProperties(productInfo, productInfoEeVo);
                productInfoEeVos.add(productInfoEeVo);
            }
            EasyExcel.write(response.getOutputStream(), ProductInfoEeVo.class).sheet("productInfo").doWrite(productInfoEeVos);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 分类页，根据分类id和查询条件显示商品
     * @param page
     * @param productInfoFrontQueryVo
     * @return
     */
    @Override
    public IPage<ProductInfo> listProductInfo(Page<ProductInfo> page, ProductInfoFrontQueryVo productInfoFrontQueryVo) {
        String categoryId = productInfoFrontQueryVo.getCategoryId(); // 分类id
        BigDecimal lowPrice = productInfoFrontQueryVo.getLowPrice(); // 最低价格
        BigDecimal highPrice = productInfoFrontQueryVo.getHighPrice(); // 最高价格
        Integer sortField = productInfoFrontQueryVo.getSortField(); // 排序字段
        Integer lostSortField = productInfoFrontQueryVo.getLostSortField(); // 上一次排序字段
        Integer sortType = productInfoFrontQueryVo.getSortType(); // 排序方式

        String typeBySort = SortTypeEnum.getTypeBySort(sortType); // 取得排序方式的字符
        String sortFieldStr = ProductSortStatusEnum.getFieldByNumber(sortField); // 取得本次排序字段
        String lostSortFieldStr = ProductSortStatusEnum.getFieldByNumber(lostSortField); // 取得上次排序字段

        QueryWrapper<ProductInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("category_id", categoryId);
        // 设置价格区间
        if (lowPrice != null) {
            wrapper.ge("promote_price", lowPrice);
        }
        if (highPrice != null) {
            wrapper.le("promote_price", highPrice);
        }
        // 定义排序条件
        if (!StringUtils.isEmpty(sortFieldStr) &&  // 首要条件是本次排序字段不为空 并且 不是默认排序
                sortField != ProductSortStatusEnum.DEFAULT.getNumber()) {

            if (SortTypeEnum.ASC.getType().equals(typeBySort)) { // 是升序排序
                wrapper.orderByAsc(sortFieldStr);
            } else { // 否则就是降序排序
                wrapper.orderByDesc(sortFieldStr);
            }
        } else if(!StringUtils.isEmpty(lostSortFieldStr) && // 如果本次排序条件为空，上次排序条件不为空也不是默认排序，则按上次排序条件排序
                lostSortField != ProductSortStatusEnum.DEFAULT.getNumber()) {

            if (SortTypeEnum.ASC.getType().equals(typeBySort)) { // 是升序排序
                wrapper.orderByAsc(lostSortFieldStr);
            } else { // 否则就是降序排序
                wrapper.orderByDesc(lostSortFieldStr);
            }
        }
        IPage<ProductInfo> productInfoIPage = baseMapper.selectProductInfoPageOrderBy(page, wrapper);

        // 对商品数据再进行处理
        productInfoIPage.getRecords().stream().forEach(item -> {
            // 封装商品参数
            this.packParams(item);
        });
        return productInfoIPage;
    }

    // 封装商品参数
    private void packParams(ProductInfo item) {
        // 把第一张缩略图封装进去
        this.packImage(item);
        // 把每个商品的名称缩短，方便前端显示（显示商品名称的前25个字符）
        item.setName(item.getName().substring(0, 25));
        // 评价数量
        QueryWrapper<Review> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", item.getId());
        int count = reviewService.count(queryWrapper);
        item.getParams().put("reviewNumber", count);
    }


    /**
     * 根据商品id查询出商品信息
     * @param id
     * @return
     */
    @Override
    public ProductInfo getProductInfoById(String id) {
        ProductInfo productInfo = baseMapper.selectById(id);
        if (productInfo == null) { // 如果查询出来为空，则说明传入的商品id不正确
            throw new TmallException(ResultCodeEnum.PARAM_ERROR);
        }
        String pid = productInfo.getId();

        // 查询评价数
        Integer countReview = reviewService.count(new QueryWrapper<Review>().eq("product_id", pid));

        // 根据商品id查询商品属性值和分类属性（在后台管理系统中已经实现，这里直接用）
        List<PropertyAndValueVo> propertyAndValueVoList = propertyValueService.show(pid);

        // 根据商品id查询出商品图片
        Map<String, List<ProductImageReturnVo>> productImage = productImageService.showByProductId(pid);

        productInfo.getParams().put("countReview", countReview);
        productInfo.getParams().put("propertyAndValueVoList", propertyAndValueVoList);
        productInfo.getParams().put("productImage", productImage);

        return productInfo;
    }

    /**
     * 商品搜索，分页显示
     * TODO 后续用搜索引擎 elastic search 实现
     * @param page
     * @param keyword
     * @return
     */
    @Override
//    @Cacheable(value = "product", keyGenerator = "keyGeneratorKeywordPage") // redis缓存
    // 这里暂时不用redis缓存，因为不同的关键字会都会添加进redis中，这样增加了redis的负担
    public IPage<ProductInfo> searchProductInfo(Page<ProductInfo> page, String keyword) {
        // 如果查询的关键字为空则直接返回空
        if (StringUtils.isEmpty(keyword)) {
            return null;
        }
        // 查询条件
        QueryWrapper<ProductInfo> wrapper = new QueryWrapper<>();
        wrapper.like("name", keyword).or().like("sub_title", keyword);
        IPage<ProductInfo> productInfoPage = baseMapper.selectPage(page, wrapper);

        // 封装商品首张缩略图 评价数
        productInfoPage.getRecords().stream().forEach(item -> {
            // 封装商品参数
            this.packParams(item);
        });

        return productInfoPage;
    }

    /**
     * 更新商品库存
     * @param productStockVo
     * @return
     */
    @Override
    public Long updateProductNumber(ProductStockVo productStockVo) {
        // 取出商品信息
        String productId = productStockVo.getProductId();
        Integer number = productStockVo.getNumber();
        ArithmeticTypeEnum type = productStockVo.getType();
        if (StringUtils.isEmpty(productId) || number == null || number <= 0) {
            throw new TmallException(ResultCodeEnum.PARAM_ERROR);
        }
        // 根据id查询商品
        ProductInfo productInfo = baseMapper.selectById(productId);
        if (productInfo == null) { // 如果查询结果为null，则抛出数据异常
            throw new TmallException(ResultCodeEnum.DATA_ERROR);
        }

        Long temp = 0L;
        if (type == ArithmeticTypeEnum.SUBTRACT) { // 减法
            temp = productInfo.getStock() - number;
            temp = temp >= 0L ? temp : productInfo.getStock();
        } else if (type == ArithmeticTypeEnum.ADD) { // 加法
            temp = productInfo.getStock() + number;
        }

        // 更新数据库
        productInfo.setStock(temp);
        int i = baseMapper.updateById(productInfo);
        if (i == 1) {
            return temp; // 库存
        }
        return null;
    }

    /**
     * 根据id查询商品，取得商品简单信息
     * @param id
     * @return
     */
    @Override
    public ProductInfo getProductSimpleInfoById(String id) {
        ProductInfo productInfo = baseMapper.selectById(id);
        if (productInfo == null) {
            throw new TmallException(ResultCodeEnum.PARAM_ERROR);
        }
        // 封装商品信息
        this.packParams(productInfo);
        return productInfo;
    }

    /**
     * 更新商品月销量，总销量
     * @param paramsMap 销量更新参数map（key:商品id   value:订单项中的商品数量）
     */
    @Override
    public void updateSales(Map<String, Integer> paramsMap) {
        // 取得商品id的集合
        Set<String> idSet = paramsMap.keySet();

        // 条件查询，取得满足条件的商品
        QueryWrapper<ProductInfo> wrapper = new QueryWrapper<>();
        wrapper.in("id", idSet);
        List<ProductInfo> productInfoList = baseMapper.selectList(wrapper);
        if (productInfoList == null || productInfoList.size() == 0) {
            throw new TmallException(ResultCodeEnum.PARAM_ERROR);
        }

        // 更新商品销量
        productInfoList.stream().forEach(item -> {
            Integer number = paramsMap.get(item.getId()); // 购买数量
            item.setSalesVolume(item.getSalesVolume() + number); // 旧总销量加购买数量
            item.setMonthlySales(item.getMonthlySales() + number); // 旧月销量加购买数量
            baseMapper.updateById(item);
        });
    }

    // 把第一张缩略图装进去
    private ProductInfo packImage(ProductInfo productInfo) {
        // 从数据库查询产品第一张缩略图的url
        String imageUrl = productImageService.getFirstThumbnailImage(productInfo.getId());
        productInfo.getParams().put("imageUrl", imageUrl);
        return productInfo;
    }
}
