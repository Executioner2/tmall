package com.study.tmall.category.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.tmall.category.mapper.CategoryInfoMapper;
import com.study.tmall.category.service.CategoryInfoService;
import com.study.tmall.category.service.PropertyService;
import com.study.tmall.exception.TmallException;
import com.study.tmall.model.category.CategoryInfo;
import com.study.tmall.model.product.ProductInfo;
import com.study.tmall.product.client.ProductFeignClient;
import com.study.tmall.result.ResultCodeEnum;
import com.study.tmall.util.FastDFSUtil;
import com.study.tmall.util.ImageUtil;
import com.study.tmall.vo.category.CategoryQueryVo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-09 19:56
 * Versions:1.0.0
 * Description:
 */
@Service
public class CategoryInfoServiceImpl extends ServiceImpl<CategoryInfoMapper, CategoryInfo> implements CategoryInfoService {
    @Resource
    private PropertyService propertyService;
    @Resource
    private ProductFeignClient productFeignClient;

    /**
     * 分页条件显示分类
     * @param page
     * @param categoryQueryVo
     * @return
     */
    @Override
    @Cacheable(value = "categoryInfo", keyGenerator = "keyGeneratorPage") // 使用spring cache 底层存入到了redis中去
    public IPage<CategoryInfo> findPageCategoryInfo(Page<CategoryInfo> page, CategoryQueryVo categoryQueryVo) {
        QueryWrapper<CategoryInfo> wrapper = new QueryWrapper<>();
        String name = categoryQueryVo.getName();
        Date createTimeBegin = categoryQueryVo.getCreateTimeBegin();
        Date createTimeEnd = categoryQueryVo.getCreateTimeEnd();

        // 如果查询名称为空，则模糊匹配分类名称
        if (!StringUtils.isEmpty(name)){
            wrapper.like("name", name);
        }
        // 如果开始日期不为空
        if (createTimeBegin != null){
            wrapper.ge("create_time", createTimeBegin);
        }
        // 如果结束日期不为空
        if (createTimeEnd != null){
            wrapper.le("create_time", createTimeEnd);
        }

        // 查询并返回结果
        IPage<CategoryInfo> categoryInfoPage = baseMapper.selectPage(page, wrapper);
        return categoryInfoPage;
    }

    /**
     * 删除分类
     * @param id
     */
    @Override
    @CacheEvict(value = "categoryInfo", allEntries = true) // 删除操作，清空redis缓存
    public void removeCategoryById(String id) {
        // 根据id查询出分类
        CategoryInfo categoryInfo = this.getById(id);
        if (categoryInfo == null){ // 如果为空，抛出异常
            throw new TmallException(ResultCodeEnum.PARAM_ERROR);
        }

        // 不能删除分类下的商品，商品数据多且重要

        // 删除分类下的属性
        propertyService.removeByCid(id);

        // 删除图片
        this.deleteFastImage(categoryInfo.getImageUrl());

        // 数据库中逻辑删除分类
        baseMapper.deleteById(categoryInfo);
    }

    /**
     * 删除fastDFS中的图片
     * @param imageUrl
     */
    private void deleteFastImage(String imageUrl) {
        if (!StringUtils.isEmpty(imageUrl)) {
            // 取得图片 服务器协议+ip  group  fastDFS中的路径
            String[] strings = ImageUtil.splitUrl(imageUrl);
            // 传入group和fastDFS中的路径 删除fastDFS中的图片
            Integer deleteResult = FastDFSUtil.delete(strings[1], strings[2]);
            // 返回0表示删除成功，否则删除失败
            if (!Objects.equals(deleteResult, 0)) {
                throw new TmallException(ResultCodeEnum.DELETE_FAIL);
            }
        }
    }

    /**
     * 批量删除分类
     * @param idList
     */
    @Override
    @CacheEvict(value = "categoryInfo", allEntries = true) // 删除操作，清空redis缓存
    public void batchRemoveCategory(List<String> idList) {
        idList.stream().forEach(item-> {
            this.removeCategoryById(item);
        });
    }

    /**
     * 编辑分类
     * @param categoryInfo
     *
     */
    @Override
    @CacheEvict(value = "categoryInfo", allEntries = true) // 编辑 操作，清空redis缓存
    public void updateCategoryById(CategoryInfo categoryInfo) {
        // 从数据库中取得分类
        CategoryInfo original = this.getById(categoryInfo);
        if (original == null) {
            throw new TmallException(ResultCodeEnum.PARAM_ERROR);
        }
        // 如果传来的分类名称为空则用以前的名称
        if (StringUtils.isEmpty(categoryInfo.getName())) {
            categoryInfo.setName(original.getName());
        }
        // 如果图片地址为空也用原来的
        if (StringUtils.isEmpty(categoryInfo.getImageUrl())){
            categoryInfo.setImageUrl(original.getImageUrl());
        } else {
            // 否则删除原来的图片
            this.deleteFastImage(original.getImageUrl());
        }
        // 更新数据库
        baseMapper.updateById(categoryInfo);
    }

    /**
     * 添加分类图片
     * @param file
     * @return
     */
    @Override
    public Map<String, String>  saveImage(MultipartFile file) {
        // 上传到fastDFS中
        try {
            Map<String, String> map = new HashMap<>();
            String filename = file.getOriginalFilename();
            String[] upload = FastDFSUtil.upload(file.getBytes(), filename);
            map.put("filename", filename); // 文件名称，包括拓展名
            map.put("imageUrl", ImageUtil.compoundUrl(upload)); // 图片url地址
            return map;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 首页显示分类列表和热销商品
     */
    @Override
    @Cacheable(value = "categoryInfo", keyGenerator = "keyGenerator") // 使用spring cache 底层存入到了redis中去
    public List<CategoryInfo> homeListCategoryInfo() {
        // 查询出所有分类
        List<CategoryInfo> categoryInfos = baseMapper.selectList(null);

        if (categoryInfos == null) {
            return null;
        }

        // 为了减少后面的远程调用次数，把所有分类的id封装到一个list集合中
        List<String> idList = new ArrayList<>();
        categoryInfos.stream().forEach(item -> {
            idList.add(item.getId());
        });

        // 根据分类分别按销量查询出热销的前5个商品信息，包含第一张缩略图url（远程调用）
        Map<String, List<ProductInfo>> hotMap = productFeignClient.listProductInfoHot(idList);

        // 根据分类分别按销量排序查询出前64个商品的小标题和id（远程调用）
        Map<String, List<ProductInfo>> productInfoSubTitleList = productFeignClient.listProductInfoSubTitle(idList);

        // 封装数据
        for (int x = 0; x < idList.size(); x++) {
            String id = idList.get(x);

            // 热销产品
            List<ProductInfo> hotList = hotMap.get(id);
            categoryInfos.get(x).getParams().put("hotList", hotList);

            // 把这64个商品的小标题做包装，以 8*8 的方式存储
            List<ProductInfo> productInfoList = productInfoSubTitleList.get(id);
            // 如果查询出来 productInfoList 为空则说明该分类没有商品，那么就跳过则此循环
            if (productInfoList == null) {
                continue;
            }
            // 浮动菜单
            List<List<Map<String, String>>> floatMenu = new ArrayList<>();
            List<Map<String, String>> rows = null; // 行
            for (int i = 0; i < productInfoList.size(); i++) {
                ProductInfo productInfo = productInfoList.get(i);
                if (i % 8 == 0) { // 一行8个，循环到8个就new一个新的行
                    rows = new ArrayList<>(); // 新的行
                    floatMenu.add(rows);
                }
                String pid = productInfo.getId(); // productId
                String subTitle = productInfo.getSubTitle(); // 小标题
                // 把小标题进行拆分，取首段
                if (StringUtils.isEmpty(subTitle)) { // 如果为空则直接置为空字符串
                    subTitle = "";
                } else {
                    subTitle = subTitle.split(" ")[0]; // 取第一段
                }
                // 存入rows的map中
                Map<String, String> item = new HashMap<>();
                item.put("id", pid);
                item.put("subTitle", subTitle);
                rows.add(item);
            }
            // 封装到 categoryInfos
            categoryInfos.get(x).getParams().put("floatMenu", floatMenu);
        }

        return categoryInfos;
    }
}
