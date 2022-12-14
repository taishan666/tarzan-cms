package com.tarzan.cms.modules.admin.service.biz;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.cms.common.constant.CoreConst;
import com.tarzan.cms.modules.admin.mapper.biz.ArticleMapper;
import com.tarzan.cms.modules.admin.model.biz.Article;
import com.tarzan.cms.modules.admin.model.biz.ArticleLook;
import com.tarzan.cms.modules.admin.model.biz.Comment;
import com.tarzan.cms.modules.admin.model.biz.Love;
import com.tarzan.cms.modules.admin.vo.ArticleConditionVo;
import com.tarzan.cms.utils.DateUtil;
import com.tarzan.cms.utils.WebUtil;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Service
public class ArticleService extends ServiceImpl<ArticleMapper, Article> {

    @Resource
    private  ArticleLookService articleLookService;
    @Resource
    private  LoveService loveService;
    @Resource
    private  CommentService commentService;

    public List<Article> findByCondition(IPage<Article> page, ArticleConditionVo vo) {
        return baseMapper.findByCondition(page, vo);
    }

    @Cacheable(value = "article", key = "'slider'")
    public List<Article> sliderList() {
        return super.lambdaQuery().select(Article::getId,Article::getCoverImage,Article::getTitle)
                .eq(Article::getStatus,CoreConst.STATUS_VALID).eq(Article::getSlider,CoreConst.STATUS_VALID).list();
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

   // @Cacheable(value = "article", key = "#id")
    public Article readById(Integer id) {
        System.out.println("测试。。");
        Article article=getById(id);
        long lookNum=articleLookService.count(Wrappers.<ArticleLook>lambdaQuery().eq(ArticleLook::getArticleId,id));
        article.setLookCount(lookNum);
        long loveNum=loveService.count(Wrappers.<Love>lambdaQuery().eq(Love::getBizId,id).eq(Love::getBizType,1));
        article.setLoveCount(loveNum);
        long commentNum= commentService.count(Wrappers.<Comment>lambdaQuery().eq(Comment::getSid,id));
        article.setCommentCount(commentNum);
        return article;
    }


    @Override
    @Cacheable(value = "article", key = "'count'")
    public long count() {
        return count(Wrappers.<Article>lambdaQuery().eq(Article::getStatus, CoreConst.STATUS_VALID));
    }

    @Cacheable(value = "article", key = "'timeline'")
    public Map<String,List<Article>> timeline() {
        List<Article> articles=list(Wrappers.<Article>lambdaQuery().select(Article::getId,Article::getTitle,Article::getCreateTime).eq(Article::getStatus, CoreConst.STATUS_VALID));
        return articles.stream().collect(Collectors.groupingBy(e-> DateUtil.format(e.getCreateTime(),"yyyy年")));
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
