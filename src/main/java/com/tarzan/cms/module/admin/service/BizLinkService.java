package com.tarzan.cms.module.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tarzan.cms.module.admin.mapper.BizLinkMapper;
import com.tarzan.cms.module.admin.model.BizLink;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
@Service
public class BizLinkService extends ServiceImpl<BizLinkMapper, BizLink> {

    @Cacheable(value = "link", key = "'list'")
    public List<BizLink> selectLinks(BizLink bizLink) {
        return baseMapper.selectList(Wrappers.<BizLink>lambdaQuery()
                .like(StringUtils.isNotBlank(bizLink.getName()),BizLink::getName, bizLink.getName())
                .like(StringUtils.isNotBlank(bizLink.getUrl()),BizLink::getUrl, bizLink.getUrl())
                .eq(Objects.nonNull(bizLink.getStatus()),BizLink::getStatus,bizLink.getStatus()));
    }

    public IPage<BizLink> pageLinks(BizLink bizLink, Integer pageNumber, Integer pageSize) {
        IPage<BizLink> page = new Page<>(pageNumber, pageSize);
        return baseMapper.selectPage(page,Wrappers.<BizLink>lambdaQuery()
                .like(StringUtils.isNotBlank(bizLink.getName()),BizLink::getName, bizLink.getName())
                .like(StringUtils.isNotBlank(bizLink.getUrl()),BizLink::getUrl, bizLink.getUrl())
                .eq(Objects.nonNull(bizLink.getStatus()),BizLink::getStatus,bizLink.getStatus()));
    }

    @CacheEvict(value = "link", allEntries = true)
    public boolean deleteBatch(List<Integer> ids) {
        return removeByIds(ids);
    }

}
