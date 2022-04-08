package com.tarzan.cms.modules.admin.service.biz;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tarzan.cms.modules.admin.mapper.biz.TagsMapper;
import com.tarzan.cms.modules.admin.model.biz.Tags;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Service
public class TagsService extends ServiceImpl<TagsMapper, Tags> {


    @Cacheable(value = "tag", key = "'list'")
    public List<Tags> selectTags(Tags tags) {
        return list(Wrappers.<Tags>lambdaQuery()
                .like(StringUtils.isNotBlank(tags.getName()), Tags::getName, tags.getName())
                .like(StringUtils.isNotBlank(tags.getDescription()), Tags::getDescription, tags.getDescription()));
    }


    @Cacheable(value = "tag", key = "'count'")
    public int count() {
        return  count(null);
    }

    public IPage<Tags> pageTags(Tags tags, Integer pageNumber, Integer pageSize) {
        IPage<Tags> page = new Page<>(pageNumber, pageSize);
        return page(page,Wrappers.<Tags>lambdaQuery()
                .like(StringUtils.isNotBlank(tags.getName()), Tags::getName, tags.getName())
                .like(StringUtils.isNotBlank(tags.getDescription()), Tags::getDescription, tags.getDescription())
                .orderByDesc(Tags::getCreateTime));
    }

    @CacheEvict(value = "tag", allEntries = true)
    public boolean deleteBatch(List<Integer> ids) {
        return removeByIds(ids);
    }
}
