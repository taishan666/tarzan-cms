package com.tarzan.cms.module.admin.service.biz;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.cms.module.admin.mapper.biz.CategoryMapper;
import com.tarzan.cms.module.admin.model.biz.Category;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
@Service
public class CategoryService extends ServiceImpl<CategoryMapper, Category> {


    @Cacheable(value = "category", key = "'tree'")
    public List<Category> selectCategories(Category bizCategory) {
        return list(Wrappers.<Category>lambdaQuery(bizCategory).orderByAsc(Category::getSort));
    }

    public Category selectById(Integer id) {
        Category category=getById(id);
        category.setParent(getById(category.getPid()));
        return category;
    }

    public List<Category> selectByPid(Integer pid) {
        return list(Wrappers.<Category>lambdaQuery().eq(Category::getPid, pid));
    }
}