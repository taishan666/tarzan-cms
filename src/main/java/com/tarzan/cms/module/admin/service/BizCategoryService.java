package com.tarzan.cms.module.admin.service;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.cms.common.constant.CoreConst;
import com.tarzan.cms.module.admin.mapper.BizCategoryMapper;
import com.tarzan.cms.module.admin.model.BizCategory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
@Service
public class BizCategoryService extends ServiceImpl<BizCategoryMapper, BizCategory> {


    @Cacheable(value = "category", key = "'tree'")
    public List<BizCategory> selectCategories(BizCategory bizCategory) {
        return list(Wrappers.<BizCategory>lambdaQuery(bizCategory).orderByAsc(BizCategory::getSort));
    }

    public BizCategory selectById(Integer id) {
        BizCategory category=getById(id);
        category.setParent(getById(category.getPid()));
        return category;
    }

    public List<BizCategory> selectByPid(Integer pid) {
        return list(Wrappers.<BizCategory>lambdaQuery().eq(BizCategory::getPid, pid));
    }
}
