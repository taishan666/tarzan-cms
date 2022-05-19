package com.tarzan.cms.modules.admin.controller.biz;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tarzan.cms.common.constant.CoreConst;
import com.tarzan.cms.common.enums.SysConfigKey;
import com.tarzan.cms.modules.admin.model.biz.Article;
import com.tarzan.cms.modules.admin.model.biz.ArticleTags;
import com.tarzan.cms.modules.admin.model.biz.Category;
import com.tarzan.cms.modules.admin.model.biz.Tags;
import com.tarzan.cms.modules.admin.model.sys.User;
import com.tarzan.cms.modules.admin.service.biz.ArticleService;
import com.tarzan.cms.modules.admin.service.biz.ArticleTagsService;
import com.tarzan.cms.modules.admin.service.biz.CategoryService;
import com.tarzan.cms.modules.admin.service.biz.TagsService;
import com.tarzan.cms.modules.admin.service.sys.SysConfigService;
import com.tarzan.cms.modules.admin.vo.ArticleConditionVo;
import com.tarzan.cms.modules.admin.vo.BaiduPushResVo;
import com.tarzan.cms.modules.admin.vo.base.PageResultVo;
import com.tarzan.cms.modules.admin.vo.base.ResponseVo;
import com.tarzan.cms.utils.PushArticleUtil;
import com.tarzan.cms.utils.ResultUtil;
import lombok.AllArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 后台文章管理
 *
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Controller
@RequestMapping("article")
@AllArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final ArticleTagsService articleTagsService;
    private final CategoryService categoryService;
    private final TagsService tagsService;
    private final SysConfigService configService;

    @PostMapping("list")
    @ResponseBody
    public PageResultVo loadArticle(ArticleConditionVo articleConditionVo, Integer pageNumber, Integer pageSize) {
        articleConditionVo.setSliderFlag(true);
        IPage<Article> page = new Page<>(pageNumber, pageSize);
        List<Article> articleList = articleService.findByCondition(page, articleConditionVo);
        return ResultUtil.table(articleList, page.getTotal());
    }

    /*文章*/
    @GetMapping("/add")
    public String addArticle(Model model) {
        List<Category> bizCategories = categoryService.selectCategories(new Category().setStatus(CoreConst.STATUS_VALID));
        List<Tags> tags = tagsService.list();
        model.addAttribute("categories", bizCategories);
        model.addAttribute("tags", tags);
        Article bizArticle = new Article().setTags(new ArrayList<>()).setOriginal(1).setSlider(0).setTop(0).setRecommended(0).setComment(1);
        model.addAttribute("article", bizArticle);
        return CoreConst.ADMIN_PREFIX + "article/publish";
    }

    @PostMapping("/add")
    @ResponseBody
    @Transactional
    public ResponseVo add(Article bizArticle,@RequestParam(value = "tagIds",required = false) List<Integer> tagIds) {
        try {
            User user = (User) SecurityUtils.getSubject().getPrincipal();
            bizArticle.setUserId(user.getId());
            bizArticle.setAuthor(user.getNickname());
            Article article = articleService.insertArticle(bizArticle);
            if(null!=tagIds && tagIds.size()>0){
                articleTagsService.insertList(tagIds, article.getId());
            }
            return ResultUtil.success("保存文章成功");
        } catch (Exception e) {
            return ResultUtil.error("保存文章失败");
        }
    }

    @GetMapping("/edit")
    public String edit(Model model, Integer id) {
        Article bizArticle = articleService.selectById(id);
        List<Category> bizCategories = categoryService.selectCategories(new Category().setStatus(CoreConst.STATUS_VALID));
        List<Tags> sTags =tagsService.list();
        List<Tags> aTags = new ArrayList<>();
        List<ArticleTags> articleTagsList=articleTagsService.list(Wrappers.<ArticleTags>lambdaQuery().eq(ArticleTags::getArticleId,id));
        if(CollectionUtils.isNotEmpty(articleTagsList)){
            List<Integer> tagsIds=articleTagsList.stream().map(ArticleTags::getTagId).collect(Collectors.toList());
            aTags = tagsService.listByIds(tagsIds);
        }
        bizArticle.setTags(aTags);
        model.addAttribute("article", bizArticle);
        model.addAttribute("categories", bizCategories);
        model.addAttribute("tags", sTags);
        return CoreConst.ADMIN_PREFIX + "article/publish";
    }

    @PostMapping("/edit")
    @ResponseBody
    @Transactional
    @CacheEvict(value = "article", allEntries = true)
    public ResponseVo edit(Article article,@RequestParam(value = "tagIds",required = false) List<Integer> tagIds) {
        articleService.updateById(article);
        if(null!=tagIds && tagIds.size()>0){
            articleTagsService.removeByArticleId(article.getId());
            articleTagsService.insertList(tagIds, article.getId());
        }
        return ResultUtil.success("编辑文章成功");
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseVo delete(Integer id) {
        return deleteBatch(Arrays.asList(id));
    }

    @PostMapping("/batch/delete")
    @ResponseBody
    public ResponseVo deleteBatch(@RequestBody List<Integer> ids) {
        boolean flag = articleService.deleteBatch(ids);
        if (flag) {
            return ResultUtil.success("删除文章成功");
        } else {
            return ResultUtil.error("删除文章失败");
        }
    }

    @PostMapping("/batch/push")
    @ResponseBody
    public ResponseVo pushBatch(@RequestParam("urls[]") String[] urls) {
        try {
            String url = configService.selectAll().get(SysConfigKey.BAIDU_PUSH_URL.getValue());
            BaiduPushResVo baiduPushResVo = JSON.parseObject(PushArticleUtil.postBaidu(url, urls), BaiduPushResVo.class);
            if (baiduPushResVo.getNotSameSite() == null && baiduPushResVo.getNotValid() == null) {
                return ResultUtil.success("推送文章成功");
            } else {
                return ResultUtil.error("推送文章失败", baiduPushResVo);
            }
        } catch (Exception e) {
            return ResultUtil.error("推送文章失败,请检查百度推送接口！");
        }

    }

}
