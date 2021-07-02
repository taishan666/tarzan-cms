package com.tarzan.cms.module.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tarzan.cms.module.admin.service.BizArticleService;
import com.tarzan.cms.module.admin.service.BizArticleTagsService;
import com.tarzan.cms.module.admin.service.BizCategoryService;
import com.tarzan.cms.common.constant.CoreConst;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tarzan.cms.utils.ResultUtil;
import com.tarzan.cms.module.admin.model.BizArticle;
import com.tarzan.cms.module.admin.model.BizCategory;
import com.tarzan.cms.module.admin.model.BizTags;
import com.tarzan.cms.module.admin.model.User;
import com.tarzan.cms.module.admin.service.BizTagsService;
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

    private final BizArticleService articleService;
    private final BizArticleTagsService articleTagsService;
    private final BizCategoryService categoryService;
    private final BizTagsService tagsService;

    @PostMapping("list")
    @ResponseBody
    public PageResultVo loadArticle(ArticleConditionVo articleConditionVo, Integer pageNumber, Integer pageSize) {
        articleConditionVo.setSliderFlag(true);
        IPage<BizArticle> page = new Page<>(pageNumber, pageSize);
        List<BizArticle> articleList = articleService.findByCondition(page, articleConditionVo);
        return ResultUtil.table(articleList, page.getTotal());
    }

    /*文章*/
    @GetMapping("/add")
    public String addArticle(Model model) {
        List<BizCategory> bizCategories = categoryService.selectCategories(new BizCategory().setStatus(CoreConst.STATUS_VALID));
        List<BizTags> tags = tagsService.list();
        model.addAttribute("categories", bizCategories);
        model.addAttribute("tags", tags);
        BizArticle bizArticle = new BizArticle().setTags(new ArrayList<>()).setOriginal(1).setSlider(0).setTop(0).setRecommended(0).setComment(1);
        model.addAttribute("article", bizArticle);
        return CoreConst.ADMIN_PREFIX + "article/publish";
    }

    @PostMapping("/add")
    @ResponseBody
    @Transactional
    @CacheEvict(value = "article", allEntries = true)
    public ResponseVo add(BizArticle bizArticle, Integer[] tags) {
        try {
            User user = (User) SecurityUtils.getSubject().getPrincipal();
            bizArticle.setUserId(user.getId());
            bizArticle.setAuthor(user.getNickname());
            BizArticle article = articleService.insertArticle(bizArticle);
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
        BizArticle bizArticle = articleService.selectById(id);
        model.addAttribute("article", bizArticle);
        List<BizCategory> bizCategories = categoryService.selectCategories(new BizCategory().setStatus(CoreConst.STATUS_VALID));
        model.addAttribute("categories", bizCategories);
        List<BizTags> sysTags = tagsService.list();
        /*方便前端处理回显*/
        List<BizTags> aTags = new ArrayList<>();
        List<BizTags> sTags = new ArrayList<>();
        boolean flag;
        for (BizTags sysTag : sysTags) {
            flag = false;
            for (BizTags articleTag : bizArticle.getTags()) {
                if (articleTag.getId().equals(sysTag.getId())) {
                    BizTags tempTags = new BizTags();
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
    public ResponseVo edit(BizArticle article, Integer[] tag) {
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
