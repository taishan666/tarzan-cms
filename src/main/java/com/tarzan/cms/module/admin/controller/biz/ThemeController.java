package com.tarzan.cms.module.admin.controller.biz;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tarzan.cms.common.constant.CoreConst;
import com.tarzan.cms.module.admin.model.biz.Theme;
import com.tarzan.cms.module.admin.service.biz.ThemeService;
import com.tarzan.cms.module.admin.vo.base.PageResultVo;
import com.tarzan.cms.module.admin.vo.base.ResponseVo;
import com.tarzan.cms.utils.ResultUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 后台主题配置
 *
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */

@Controller
@RequestMapping("theme")
@AllArgsConstructor
public class ThemeController {

    private final ThemeService bizThemeService;


    @PostMapping("page")
    @ResponseBody
    public PageResultVo loadTheme(Integer pageNumber, Integer pageSize) {
        IPage<Theme> page = new Page<>(pageNumber, pageSize);
        page = bizThemeService.page(page);
        return ResultUtil.table(page.getRecords(), page.getTotal());
    }

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("themes", bizThemeService.list());
        return CoreConst.ADMIN_PREFIX + "theme/list";
    }

    @GetMapping("/add")
    public String add() {
        return CoreConst.ADMIN_PREFIX + "theme/form";
    }
    @ResponseBody
    @PostMapping("/upload")
    public ResponseVo upload(@RequestParam(value = "file", required = false) MultipartFile file) {
        return bizThemeService.upload(file);
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
        Theme theme=bizThemeService.getById(id);
        Assert.isNull(theme,"主题不存在！");
        if(theme.getStatus()==CoreConst.STATUS_VALID){
            return ResultUtil.error("启用主题不能删除");
        }
        boolean flag = bizThemeService.delete(id);
        if (flag) {
            return ResultUtil.success("删除主题成功");
        } else {
            return ResultUtil.error("删除主题失败");
        }
    }


}
