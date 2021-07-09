package com.tarzan.cms.module.admin.controller.biz;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tarzan.cms.common.constant.CoreConst;
import com.tarzan.cms.utils.ResultUtil;
import com.tarzan.cms.module.admin.model.biz.Theme;
import com.tarzan.cms.module.admin.service.biz.ThemeService;
import com.tarzan.cms.module.admin.vo.base.PageResultVo;
import com.tarzan.cms.module.admin.vo.base.ResponseVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 后台主题配置
 *
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
@Controller
@RequestMapping("theme")
@AllArgsConstructor
public class ThemeController {

    private final ThemeService bizThemeService;


    @PostMapping("list")
    @ResponseBody
    public PageResultVo loadTheme(Integer pageNumber, Integer pageSize) {
        IPage<Theme> page = new Page<>(pageNumber, pageSize);
        page = bizThemeService.page(page);
        return ResultUtil.table(page.getRecords(), page.getTotal());
    }

    @GetMapping("/add")
    public String add() {
        return CoreConst.ADMIN_PREFIX + "theme/form";
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseVo add(Theme theme) {
        Date date = new Date();
        theme.setCreateTime(date);
        theme.setUpdateTime(date);
        theme.setStatus(CoreConst.STATUS_INVALID);
        boolean flag = bizThemeService.save(theme);
        if (flag) {
            return ResultUtil.success("新增主题成功");
        } else {
            return ResultUtil.error("新增主题失败");
        }
    }

    @GetMapping("/edit")
    public String edit(Model model, Integer id) {
        Theme theme = bizThemeService.getById(id);
        model.addAttribute("theme", theme);
        return CoreConst.ADMIN_PREFIX + "theme/form";
    }

    @PostMapping("/edit")
    @ResponseBody
    public ResponseVo edit(Theme theme) {
        theme.setUpdateTime(new Date());
        boolean flag = bizThemeService.updateById(theme);
        if (flag) {
            return ResultUtil.success("编辑主题成功");
        } else {
            return ResultUtil.error("编辑主题失败");
        }
    }

    @PostMapping("/use")
    @ResponseBody
    public ResponseVo use(Integer id) {
        boolean flag = bizThemeService.useTheme(id);
        if (flag) {
            return ResultUtil.success("启用主题成功");
        } else {
            return ResultUtil.error("启用主题失败");
        }
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseVo delete(Integer id) {
        boolean flag = bizThemeService.removeById(id);
        if (flag) {
            return ResultUtil.success("删除主题成功");
        } else {
            return ResultUtil.error("删除主题失败");
        }
    }

    @PostMapping("/batch/delete")
    @ResponseBody
    public ResponseVo deleteBatch(@RequestParam("ids") List<Integer> ids) {
        boolean flag = bizThemeService.deleteBatch(ids);
        if (flag) {
            return ResultUtil.success("删除主题成功");
        } else {
            return ResultUtil.error("删除主题失败");
        }
    }

}