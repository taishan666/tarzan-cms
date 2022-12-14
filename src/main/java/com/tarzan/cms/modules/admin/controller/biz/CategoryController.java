package com.tarzan.cms.modules.admin.controller.biz;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.tarzan.cms.cache.CategoryCache;
import com.tarzan.cms.common.constant.CoreConst;
import com.tarzan.cms.utils.ResultUtil;
import com.tarzan.cms.modules.admin.model.biz.Article;
import com.tarzan.cms.modules.admin.model.biz.Category;
import com.tarzan.cms.modules.admin.service.biz.ArticleService;
import com.tarzan.cms.modules.admin.service.biz.CategoryService;
import com.tarzan.cms.modules.admin.vo.base.ResponseVo;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 后台类目管理
 *
 * @author tarzan liu
 * @since JDK1.8
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
    public List<Category> loadCategory() {
        return categoryService.selectCategories(CoreConst.STATUS_VALID);
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("categories", getCategories());
        return CoreConst.ADMIN_PREFIX + "category/form";
    }

    @PostMapping("/add")
    @ResponseBody
    @CacheEvict(value = "category", allEntries = true)
    public ResponseVo add(Category bizCategory) {
        if (existArticles(bizCategory.getPid())) {
                return ResultUtil.error("添加失败，父级分类下存在文章");
        }
        Date date = new Date();
        bizCategory.setCreateTime(date);
        bizCategory.setUpdateTime(date);
        bizCategory.setStatus(CoreConst.STATUS_VALID);
        boolean flag = categoryService.save(bizCategory);
        if (flag) {
            CategoryCache.save(bizCategory);
            return ResultUtil.success("新增分类成功");
        } else {
            return ResultUtil.error("新增分类失败");
        }
    }

    @GetMapping("/edit")
    public String edit(Model model, Integer id) {
        Category bizCategory = categoryService.selectById(id);
        model.addAttribute("category", bizCategory);
        model.addAttribute("categories", getCategories());
        return CoreConst.ADMIN_PREFIX + "category/form";
    }


    @PostMapping("/edit")
    @ResponseBody
    @CacheEvict(value = "category", allEntries = true)
    public ResponseVo edit(Category bizCategory) {
        if (existArticles(bizCategory.getPid())) {
            return ResultUtil.error("编辑失败，父级分类下存在文章");
        }
        bizCategory.setUpdateTime(new Date());
        boolean flag = categoryService.updateById(bizCategory);
        if (flag) {
            CategoryCache.save(bizCategory);
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
            CategoryCache.delete(id);
            return ResultUtil.success("删除分类成功");
        } else {
            return ResultUtil.error("删除分类失败");
        }
    }

    private List<Category> getCategories(){
        return categoryService.selectCategories(CoreConst.STATUS_VALID);
    }

    private boolean existArticles(Integer id){
        if (!CoreConst.TOP_MENU_ID.equals(id)) {
            List<Article> bizArticles = articleService.selectByCategoryId(id);
            return CollectionUtils.isNotEmpty(bizArticles);
        }
        return false;
    }

}
