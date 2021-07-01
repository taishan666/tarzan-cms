package com.tarzan.cms.module.admin.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.cms.module.admin.mapper.BizArticleTagsMapper;
import com.tarzan.cms.module.admin.model.BizArticleTags;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
@Service
public class BizArticleTagsService extends ServiceImpl<BizArticleTagsMapper, BizArticleTags> {


    public int removeByArticleId(Integer articleId) {
        return baseMapper.delete(Wrappers.<BizArticleTags>lambdaQuery().eq(BizArticleTags::getArticleId, articleId));
    }

    public void insertList(Integer[] tagIds, Integer articleId) {
        Date date = new Date();
        for (Integer tagId : tagIds) {
            BizArticleTags articleTags = new BizArticleTags();
            articleTags.setTagId(tagId);
            articleTags.setArticleId(articleId);
            articleTags.setCreateTime(date);
            articleTags.setUpdateTime(date);
            baseMapper.insert(articleTags);
        }
    }
}
