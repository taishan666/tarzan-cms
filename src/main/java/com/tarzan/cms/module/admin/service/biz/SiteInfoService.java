package com.tarzan.cms.module.admin.service.biz;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tarzan.cms.common.constant.CoreConst;
import com.tarzan.cms.module.admin.mapper.biz.ArticleMapper;
import com.tarzan.cms.module.admin.mapper.biz.CategoryMapper;
import com.tarzan.cms.module.admin.mapper.biz.CommentMapper;
import com.tarzan.cms.module.admin.mapper.biz.TagsMapper;
import com.tarzan.cms.module.admin.model.biz.Article;
import com.tarzan.cms.module.admin.model.biz.Category;
import com.tarzan.cms.module.admin.model.biz.Comment;
import com.tarzan.cms.module.admin.model.biz.Tags;
import com.tarzan.cms.module.admin.vo.SiteInfoVo;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


/**
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
@Service
@AllArgsConstructor
public class SiteInfoService {

    private final ArticleMapper articleMapper;
    private final TagsMapper tagsMapper;
    private final CategoryMapper categoryMapper;
    private final CommentMapper commentMapper;

    public SiteInfoVo getSiteInfo() {
        SiteInfoVo siteInfoVo=new SiteInfoVo();
        Integer articleCount=articleMapper.selectCount(Wrappers.<Article>lambdaQuery().eq(Article::getStatus, CoreConst.STATUS_VALID));
        Integer tagsCount=tagsMapper.selectCount(null);
        Integer categoryCount=categoryMapper.selectCount(Wrappers.<Category>lambdaQuery().eq(Category::getStatus, CoreConst.STATUS_VALID));
        Integer commentCount=commentMapper.selectCount(Wrappers.<Comment>lambdaQuery().eq(Comment::getStatus, CoreConst.STATUS_VALID));
        siteInfoVo.setArticleCount(articleCount);
        siteInfoVo.setTagCount(tagsCount);
        siteInfoVo.setCategoryCount(categoryCount);
        siteInfoVo.setCommentCount(commentCount);
        return siteInfoVo;
    }

}
