package com.tarzan.module.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tarzan.common.util.CoreConst;
import com.tarzan.common.util.ResultUtil;
import com.tarzan.module.admin.model.BizLink;
import com.tarzan.module.admin.service.BizLinkService;
import com.tarzan.module.admin.vo.base.PageResultVo;
import com.tarzan.module.admin.vo.base.ResponseVo;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 后台友情链接管理
 *
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
@Controller
@RequestMapping("link")
@AllArgsConstructor
public class LinksController {

    private final BizLinkService linkService;

    @PostMapping("list")
    @ResponseBody
    public PageResultVo loadLinks(BizLink bizLink, Integer pageNumber, Integer pageSize) {
        IPage<BizLink> bizLinkPage = linkService.pageLinks(bizLink, pageNumber, pageSize);
        return ResultUtil.table(bizLinkPage.getRecords(), bizLinkPage.getTotal());
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("link", new BizLink().setStatus(1));
        return CoreConst.ADMIN_PREFIX + "link/form";
    }

    @PostMapping("/add")
    @ResponseBody
    @CacheEvict(value = "link", allEntries = true)
    public ResponseVo add(BizLink bizLink) {
        Date date = new Date();
        bizLink.setCreateTime(date);
        bizLink.setUpdateTime(date);
        boolean i = linkService.save(bizLink);
        if (i) {
            return ResultUtil.success("新增友链成功");
        } else {
            return ResultUtil.error("新增友链失败");
        }
    }

    @GetMapping("/edit")
    public String edit(Model model, Integer id) {
        BizLink bizLink = linkService.getById(id);
        model.addAttribute("link", bizLink);
        return CoreConst.ADMIN_PREFIX + "link/form";
    }

    @PostMapping("/edit")
    @ResponseBody
    @CacheEvict(value = "link", allEntries = true)
    public ResponseVo edit(BizLink bizLink) {
        bizLink.setUpdateTime(new Date());
        boolean i = linkService.updateById(bizLink);
        if (i) {
            return ResultUtil.success("编辑友链成功");
        } else {
            return ResultUtil.error("编辑友链失败");
        }
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseVo delete(Integer id) {
        return deleteBatch(new Integer[]{id});
    }

    @PostMapping("/batch/delete")
    @ResponseBody
    public ResponseVo deleteBatch(@RequestParam("ids[]") Integer[] ids) {
        int i = linkService.deleteBatch(ids);
        if (i > 0) {
            return ResultUtil.success("删除友链成功");
        } else {
            return ResultUtil.error("删除友链失败");
        }
    }

}
