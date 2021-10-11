package com.tarzan.cms.module.admin.service.biz;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.cms.common.constant.CoreConst;
import com.tarzan.cms.module.admin.mapper.biz.ArticleMapper;
import com.tarzan.cms.module.admin.model.biz.Article;
import com.tarzan.cms.module.admin.vo.ArticleConditionVo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Service
public class ArticleService extends ServiceImpl<ArticleMapper, Article> {

    public List<Article> findByCondition(IPage<Article> page, ArticleConditionVo vo) {
        return baseMapper.findByCondition(page, vo);
    }

    @Cacheable(value = "article", key = "'slider'")
    public List<Article> sliderList() {
        ArticleConditionVo vo = new ArticleConditionVo();
        vo.setSlider(true);
        vo.setStatus(CoreConst.STATUS_VALID);
        return this.findByCondition(null, vo);
    }

    @Cacheable(value = "article", key = "'recommended'")
    public List<Article> recommendedList(int pageSize) {
        ArticleConditionVo vo = new ArticleConditionVo();
        vo.setRecommended(true);
        vo.setStatus(CoreConst.STATUS_VALID);
        vo.setPageSize(pageSize);
        IPage<Article> page = new Page<>(vo.getPageNumber(), vo.getPageSize());
        return this.findByCondition(page, vo);
    }

    @Cacheable(value = "article", key = "'recent'")
    public List<Article> recentList(int pageSize) {
        ArticleConditionVo vo = new ArticleConditionVo();
        vo.setRecentFlag(true);
        vo.setStatus(CoreConst.STATUS_VALID);
        vo.setPageSize(pageSize);
        IPage<Article> page = new Page<>(vo.getPageNumber(), vo.getPageSize());
        return this.findByCondition(page, vo);
    }


    @Cacheable(value = "article", key = "'hot'")
    public List<Article> hotList(int pageSize) {
        IPage<Article> page = new Page<>(1, pageSize);
        return baseMapper.hotList(page);
    }

    @Cacheable(value = "article", key = "#id")
    public Article selectById(Integer id) {
        return getById(id);
    }

    @Cacheable(value = "article", key = "'count'")
    public int count() {
        return count(Wrappers.<Article>lambdaQuery().eq(Article::getStatus, CoreConst.STATUS_VALID));
    }

    @CacheEvict(value = "article", allEntries = true)
    public Article insertArticle(Article bizArticle) {
        Date date = new Date();
        bizArticle.setCreateTime(date);
        bizArticle.setUpdateTime(date);
        save(bizArticle);
        return bizArticle;
    }

    public List<Article> selectByCategoryId(Integer categoryId) {
        return list(Wrappers.<Article>lambdaQuery().eq(Article::getCategoryId, categoryId));
    }

    @CacheEvict(value = "article", allEntries = true)
    public boolean deleteBatch(List<Integer> ids){
        return removeByIds(ids);
    }

}
