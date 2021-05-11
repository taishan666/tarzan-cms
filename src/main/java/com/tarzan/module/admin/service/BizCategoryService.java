package com.tarzan.module.admin.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.module.admin.mapper.BizCategoryMapper;
import com.tarzan.module.admin.model.BizCategory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
@Service
public class BizCategoryService extends ServiceImpl<BizCategoryMapper, BizCategory> {


    @Cacheable(value = "category", key = "'tree'")
    public List<BizCategory> selectCategories(BizCategory bizCategory) {
        return baseMapper.selectCategories(bizCategory);
    }

    @CacheEvict(value = "category", allEntries = true)
    public int deleteBatch(Integer[] ids) {
        return baseMapper.deleteBatch(ids);
    }

    public BizCategory selectById(Integer id) {
        return baseMapper.getById(id);
    }

    public List<BizCategory> selectByPid(Integer pid) {
        return baseMapper.selectList(Wrappers.<BizCategory>lambdaQuery().eq(BizCategory::getPid, pid));
    }
}
