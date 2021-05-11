package com.tarzan.module.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.common.util.Pagination;
import com.tarzan.module.admin.mapper.BizLinkMapper;
import com.tarzan.module.admin.model.BizLink;
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
public class BizLinkService extends ServiceImpl<BizLinkMapper, BizLink> {

    @Cacheable(value = "link", key = "'list'")
    public List<BizLink> selectLinks(BizLink bizLink) {
        return baseMapper.selectLinks(null, bizLink);
    }

    public IPage<BizLink> pageLinks(BizLink bizLink, Integer pageNumber, Integer pageSize) {
        IPage<BizLink> page = new Pagination<>(pageNumber, pageSize);
        page.setRecords(baseMapper.selectLinks(page, bizLink));
        return page;
    }

    @CacheEvict(value = "link", allEntries = true)
    public int deleteBatch(Integer[] ids) {
        return baseMapper.deleteBatch(ids);
    }

}
