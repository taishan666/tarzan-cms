package com.tarzan.cms.module.admin.service.biz;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.cms.module.admin.mapper.biz.ArticleTagsMapper;
import com.tarzan.cms.module.admin.model.biz.ArticleTags;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
@Service
public class ArticleTagsService extends ServiceImpl<ArticleTagsMapper, ArticleTags> {


    public boolean removeByArticleId(Integer articleId) {
        return remove(Wrappers.<ArticleTags>lambdaQuery().eq(ArticleTags::getArticleId, articleId));
    }

    public void insertList(Integer[] tagIds, Integer articleId) {
        Date date = new Date();
        for (Integer tagId : tagIds) {
            ArticleTags articleTags = new ArticleTags();
            articleTags.setTagId(tagId);
            articleTags.setArticleId(articleId);
            articleTags.setCreateTime(date);
            articleTags.setUpdateTime(date);
            baseMapper.insert(articleTags);
        }
    }
}