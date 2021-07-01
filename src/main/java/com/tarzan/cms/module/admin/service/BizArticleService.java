package com.tarzan.cms.module.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.cms.common.constant.CoreConst;
import com.tarzan.cms.module.admin.mapper.BizArticleMapper;
import com.tarzan.cms.module.admin.model.BizArticle;
import com.tarzan.cms.module.admin.vo.ArticleConditionVo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
@Service
public class BizArticleService extends ServiceImpl<BizArticleMapper, BizArticle> {


    public List<BizArticle> findByCondition(IPage<BizArticle> page, ArticleConditionVo vo) {
        List<BizArticle> list = baseMapper.findByCondition(page, vo);
        if (CollectionUtils.isNotEmpty(list)) {
            List<Integer> ids = new ArrayList<>();
            for (BizArticle bizArticle : list) {
                ids.add(bizArticle.getId());
            }
            List<BizArticle> listTag = baseMapper.listTagsByArticleId(ids);
            // listTag, 重新组装数据为{id: Article}
            Map<Integer, BizArticle> tagMap = new LinkedHashMap<>(listTag.size());
            for (BizArticle bizArticle : listTag) {
                tagMap.put(bizArticle.getId(), bizArticle);
            }

            for (BizArticle bizArticle : list) {
                BizArticle tagArticle = tagMap.get(bizArticle.getId());
                if (Objects.nonNull(tagArticle)) {
                    bizArticle.setTags(tagArticle.getTags());
                }
            }
        }
        return list;
    }

    @Cacheable(value = "article", key = "'slider'")
    public List<BizArticle> sliderList() {
        ArticleConditionVo vo = new ArticleConditionVo();
        vo.setSlider(true);
        vo.setStatus(CoreConst.STATUS_VALID);
        return this.findByCondition(null, vo);
    }

    @Cacheable(value = "article", key = "'recommended'")
    public List<BizArticle> recommendedList(int pageSize) {
        ArticleConditionVo vo = new ArticleConditionVo();
        vo.setRecommended(true);
        vo.setStatus(CoreConst.STATUS_VALID);
        vo.setPageSize(pageSize);
        IPage<BizArticle> page = new Page<>(vo.getPageNumber(), vo.getPageSize());
        return this.findByCondition(page, vo);
    }

    @Cacheable(value = "article", key = "'recent'")
    public List<BizArticle> recentList(int pageSize) {
        ArticleConditionVo vo = new ArticleConditionVo();
        vo.setPageSize(pageSize);
        vo.setStatus(CoreConst.STATUS_VALID);
        vo.setRecentFlag(true);
        IPage<BizArticle> page = new Page<>(vo.getPageNumber(), vo.getPageSize());
        return this.findByCondition(page, vo);
    }

    @Cacheable(value = "article", key = "'random'")
    public List<BizArticle> randomList(int pageSize) {
        ArticleConditionVo vo = new ArticleConditionVo();
        vo.setRandom(true);
        vo.setStatus(CoreConst.STATUS_VALID);
        vo.setPageSize(pageSize);
        IPage<BizArticle> page = new Page<>(vo.getPageNumber(), vo.getPageSize());
        return this.findByCondition(page, vo);
    }

    @Cacheable(value = "article", key = "'hot'")
    public List<BizArticle> hotList(int pageSize) {
        IPage<BizArticle> page = new Page<>(1, pageSize);
        return baseMapper.hotList(page);
    }

    @Cacheable(value = "article", key = "#id")
    public BizArticle selectById(Integer id) {
        return baseMapper.getById(id);
    }

    @CacheEvict(value = "article", allEntries = true)
    public BizArticle insertArticle(BizArticle bizArticle) {
        Date date = new Date();
        bizArticle.setCreateTime(date);
        bizArticle.setUpdateTime(date);
        baseMapper.insert(bizArticle);
        return bizArticle;
    }

    public List<BizArticle> selectByCategoryId(Integer categoryId) {
        return baseMapper.selectList(Wrappers.<BizArticle>lambdaQuery().eq(BizArticle::getCategoryId, categoryId));
    }

}
