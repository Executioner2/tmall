package com.study.tmall.category.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.tmall.category.mapper.CategoryInfoMapper;
import com.study.tmall.category.service.CategoryInfoService;
import com.study.tmall.exception.TmallException;
import com.study.tmall.model.category.CategoryInfo;
import com.study.tmall.result.ResultCodeEnum;
import com.study.tmall.util.FastDFSUtil;
import com.study.tmall.util.ImageUtil;
import com.study.tmall.vo.category.CategoryQueryVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-09 19:56
 * Versions:1.0.0
 * Description:
 */
@Service
public class CategoryInfoServiceImpl extends ServiceImpl<CategoryInfoMapper, CategoryInfo> implements CategoryInfoService {

    /**
     * 分页条件显示分类
     * @param page
     * @param categoryQueryVo
     * @return
     */
    @Override
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
        if (createTimeBegin != null){
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
    public void removeCategoryById(String id) {
        // 根据id查询出分类
        CategoryInfo categoryInfo = this.getById(id);
        if (categoryInfo == null){ // 如果为空，抛出异常
            throw new TmallException(ResultCodeEnum.PARAM_ERROR);
        }

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
    public void batchRemoveCategory(List<String> idList) {
        idList.stream().forEach(item-> {
            this.removeCategoryById(item);
        });
    }

    /**
     * 编辑分类
     * @param categoryInfo
     * @param file
     */
    @Override
    public void updateCategoryById(CategoryInfo categoryInfo, MultipartFile file) {
        try {
            // 从数据库中取得分类
            CategoryInfo original = this.getById(categoryInfo);
            if (original == null){
                throw new TmallException(ResultCodeEnum.PARAM_ERROR);
            }
            // 如果传来的分类名称为空则用以前的名称
            if (StringUtils.isEmpty(categoryInfo.getName())){
                categoryInfo.setName(original.getName());
            }
            // 把新的图片上传到fdfs
            if (file != null) {
                // 删除原来的图片
                this.deleteFastImage(original.getImageUrl());
                String[] upload = FastDFSUtil.upload(file.getBytes(), ImageUtil.getFileExtName(file));
                categoryInfo.setImageUrl(ImageUtil.compoundUrl(upload));
            }
            baseMapper.updateById(categoryInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加分类
     * @param categoryInfo
     * @param file
     */
    @Override
    public void saveCategory(CategoryInfo categoryInfo, MultipartFile file) {
        try {
            if (file != null) {
                // 上传到fastDFS中
                String[] upload = FastDFSUtil.upload(file.getBytes(), ImageUtil.getFileExtName(file));
                categoryInfo.setImageUrl(ImageUtil.compoundUrl(upload)); // 设置最终可直接访问的imageUrl地址
            }
            baseMapper.insert(categoryInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
