package com.tarzan.cms.module.admin.controller.biz;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tarzan.cms.module.admin.service.biz.ArticleService;
import com.tarzan.cms.module.admin.service.biz.ArticleTagsService;
import com.tarzan.cms.module.admin.service.biz.CategoryService;
import com.tarzan.cms.common.constant.CoreConst;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tarzan.cms.utils.ResultUtil;
import com.tarzan.cms.module.admin.model.biz.Article;
import com.tarzan.cms.module.admin.model.biz.Category;
import com.tarzan.cms.module.admin.model.biz.Tags;
import com.tarzan.cms.module.admin.model.sys.User;
import com.tarzan.cms.module.admin.service.biz.TagsService;
import com.tarzan.cms.module.admin.vo.ArticleConditionVo;
import com.tarzan.cms.module.admin.vo.base.PageResultVo;
import com.tarzan.cms.module.admin.vo.base.ResponseVo;
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

/**
 * 后台文章管理
 *
 * @author tarzan liu
 * @version V1.0
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
    @CacheEvict(value = "article", allEntries = true)
    public ResponseVo add(Article bizArticle, Integer[] tags) {
        try {
            User user = (User) SecurityUtils.getSubject().getPrincipal();
            bizArticle.setUserId(user.getId());
            bizArticle.setAuthor(user.getNickname());
            Article article = articleService.insertArticle(bizArticle);
            if(null!=tags && tags.length>0){
                articleTagsService.insertList(tags, article.getId());
            }
            return ResultUtil.success("保存文章成功");
        } catch (Exception e) {
            return ResultUtil.error("保存文章失败");
        }
    }

    @GetMapping("/edit")
    public String edit(Model model, Integer id) {
        Article bizArticle = articleService.selectById(id);
        model.addAttribute("article", bizArticle);
        List<Category> bizCategories = categoryService.selectCategories(new Category().setStatus(CoreConst.STATUS_VALID));
        model.addAttribute("categories", bizCategories);
        List<Tags> sysTags = tagsService.list();
        /*方便前端处理回显*/
        List<Tags> aTags = new ArrayList<>();
        List<Tags> sTags = new ArrayList<>();
        boolean flag;
        for (Tags sysTag : sysTags) {
            flag = false;
            for (Tags articleTag : bizArticle.getTags()) {
                if (articleTag.getId().equals(sysTag.getId())) {
                    Tags tempTags = new Tags();
                    tempTags.setId(sysTag.getId());
                    tempTags.setName(sysTag.getName());
                    aTags.add(tempTags);
                    sTags.add(tempTags);
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                sTags.add(sysTag);
            }
        }
        bizArticle.setTags(aTags);
        model.addAttribute("tags", sTags);
        return CoreConst.ADMIN_PREFIX + "article/publish";
    }

    @PostMapping("/edit")
    @ResponseBody
    @CacheEvict(value = "article", allEntries = true)
    public ResponseVo edit(Article article, Integer[] tag) {
        articleService.updateById(article);
        articleTagsService.removeByArticleId(article.getId());
        articleTagsService.insertList(tag, article.getId());
        return ResultUtil.success("编辑文章成功");
    }

    @PostMapping("/delete")
    @ResponseBody
    @CacheEvict(value = "article", allEntries = true)
    public ResponseVo delete(Integer id) {
        return deleteBatch(Arrays.asList(id));
    }

    @PostMapping("/batch/delete")
    @ResponseBody
    @CacheEvict(value = "article", allEntries = true)
    public ResponseVo deleteBatch(@RequestParam("ids") List<Integer> ids) {
        boolean flag = articleService.removeByIds(ids);
        if (flag) {
            return ResultUtil.success("删除文章成功");
        } else {
            return ResultUtil.error("删除文章失败");
        }
    }

}
