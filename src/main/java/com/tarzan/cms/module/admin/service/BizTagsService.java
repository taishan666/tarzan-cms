package com.tarzan.cms.module.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tarzan.cms.module.admin.mapper.BizTagsMapper;
import com.tarzan.cms.module.admin.model.BizTags;
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
public class BizTagsService extends ServiceImpl<BizTagsMapper, BizTags> {


    @Cacheable(value = "tag", key = "'list'")
    public List<BizTags> selectTags(BizTags bizTags) {
        return baseMapper.selectList(Wrappers.<BizTags>lambdaQuery()
                .like(StringUtils.isNotBlank(bizTags.getName()),BizTags::getName,bizTags.getName())
                .like(StringUtils.isNotBlank(bizTags.getDescription()),BizTags::getDescription,bizTags.getDescription()));
    }

    public IPage<BizTags> pageTags(BizTags bizTags, Integer pageNumber, Integer pageSize) {
        IPage<BizTags> page = new Page<>(pageNumber, pageSize);
        return baseMapper.selectPage(page,Wrappers.<BizTags>lambdaQuery()
                .like(StringUtils.isNotBlank(bizTags.getName()),BizTags::getName,bizTags.getName())
                .like(StringUtils.isNotBlank(bizTags.getDescription()),BizTags::getDescription,bizTags.getDescription()));
    }

    @CacheEvict(value = "tag", allEntries = true)
    public boolean deleteBatch(List<Integer> ids) {
        return removeByIds(ids);
    }
}
