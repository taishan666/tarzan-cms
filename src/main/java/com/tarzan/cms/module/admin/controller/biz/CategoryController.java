package com.tarzan.cms.module.admin.controller.biz;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.tarzan.cms.common.constant.CoreConst;
import com.tarzan.cms.utils.ResultUtil;
import com.tarzan.cms.module.admin.model.biz.Article;
import com.tarzan.cms.module.admin.model.biz.Category;
import com.tarzan.cms.module.admin.service.biz.ArticleService;
import com.tarzan.cms.module.admin.service.biz.CategoryService;
import com.tarzan.cms.module.admin.vo.base.ResponseVo;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.ListUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 后台类目管理
 *
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
@Controller
@RequestMapping("category")
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final ArticleService articleService;

    @PostMapping("list")
    @ResponseBody
    public List<Category> loadCategory(boolean isFistLevel) {
        Category bizCategory = new Category();
        bizCategory.setStatus(CoreConst.STATUS_VALID);
        if (isFistLevel) {
            bizCategory.setPid(CoreConst.TOP_MENU_ID);
        }
        return categoryService.selectCategories(bizCategory);
    }

    @GetMapping("/add")
    public String add() {
        return CoreConst.ADMIN_PREFIX + "category/form";
    }

    @PostMapping("/add")
    @ResponseBody
    @CacheEvict(value = "category", allEntries = true)
    public ResponseVo add(Category bizCategory) {
        if (!CoreConst.TOP_MENU_ID.equals(bizCategory.getPid())) {
            List<Article> bizArticles = articleService.selectByCategoryId(bizCategory.getPid());
            if (!CollectionUtils.isEmpty(bizArticles)) {
                return ResultUtil.error("添加失败，父级分类下存在文章");
            }
        }
        Date date = new Date();
        bizCategory.setCreateTime(date);
        bizCategory.setUpdateTime(date);
        bizCategory.setStatus(CoreConst.STATUS_VALID);
        boolean flag = categoryService.save(bizCategory);
        if (flag) {
            return ResultUtil.success("新增分类成功");
        } else {
            return ResultUtil.error("新增分类失败");
        }
    }

    @GetMapping("/edit")
    public String edit(Model model, Integer id) {
        Category bizCategory = categoryService.selectById(id);
        model.addAttribute("category", bizCategory);
        return CoreConst.ADMIN_PREFIX + "category/form";
    }

    @PostMapping("/edit")
    @ResponseBody
    @CacheEvict(value = "category", allEntries = true)
    public ResponseVo edit(Category bizCategory) {
        bizCategory.setUpdateTime(new Date());
        boolean flag = categoryService.updateById(bizCategory);
        if (flag) {
            return ResultUtil.success("编辑分类成功");
        } else {
            return ResultUtil.error("编辑分类失败");
        }
    }

    @PostMapping("/delete")
    @ResponseBody
    @CacheEvict(value = "category", allEntries = true)
    public ResponseVo delete(Integer id) {
        if (CollectionUtils.isNotEmpty(categoryService.selectByPid(id))) {
            return ResultUtil.error("该分类下存在子分类！");
        }
        boolean flag = categoryService.removeById(id);
        if (flag) {
            return ResultUtil.success("删除分类成功");
        } else {
            return ResultUtil.error("删除分类失败");
        }
    }


}
