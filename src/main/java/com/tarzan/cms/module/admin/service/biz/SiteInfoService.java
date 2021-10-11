package com.tarzan.cms.module.admin.service.biz;

import com.tarzan.cms.module.admin.vo.SiteInfoVo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Service
@AllArgsConstructor
public class SiteInfoService {

    private final ArticleService articleService;
    private final TagsService tagsService;
    private final CategoryService categoryService;
    private final CommentService commentService;

    public SiteInfoVo getSiteInfo() {
        SiteInfoVo siteInfoVo=new SiteInfoVo();
        siteInfoVo.setArticleCount(articleService.count());
        siteInfoVo.setTagCount(tagsService.count());
        siteInfoVo.setCategoryCount(categoryService.count());
        siteInfoVo.setCommentCount(commentService.count());
        return siteInfoVo;
    }

}
