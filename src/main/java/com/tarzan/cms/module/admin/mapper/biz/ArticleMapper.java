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
 * @version V1.0
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
     * 统计指定文章的标签集合
     *
     * @param list
     * @return
     */
    List<Article> listTagsByArticleId(List<Integer> list);

    /**
     * 热门文章
     *
     * @param page
     * @return
     */
    List<Article> hotList(@Param("page") IPage<Article> page);

    /**
     * 获取文章详情，文章标签、文章类型
     *
     * @param id
     * @return
     */
    Article getById(Integer id);


    /**
     * 统计网站信息
     *
     * @return
     */
    SiteInfoVo getSiteInfo();
}
