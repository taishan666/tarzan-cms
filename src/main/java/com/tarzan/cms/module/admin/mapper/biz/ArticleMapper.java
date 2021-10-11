package com.tarzan.cms.module.admin.mapper.biz;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tarzan.cms.module.admin.model.biz.Article;
import com.tarzan.cms.module.admin.vo.ArticleConditionVo;
import com.tarzan.cms.module.admin.vo.SiteInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 分页查询，关联查询文章标签、文章类型
     *
     * @param page
     * @param vo
     * @return
     */
    List<Article> findByCondition(@Param("page") IPage<Article> page, @Param("vo") ArticleConditionVo vo);

    /**
     * 热门文章
     *
     * @param page
     * @return
     */
    List<Article> hotList(@Param("page") IPage<Article> page);




}
