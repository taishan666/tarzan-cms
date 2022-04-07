package com.tarzan.cms.modules.admin.controller.biz;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tarzan.cms.common.constant.CoreConst;
import com.tarzan.cms.utils.ResultUtil;
import com.tarzan.cms.modules.admin.model.biz.Tags;
import com.tarzan.cms.modules.admin.service.biz.TagsService;
import com.tarzan.cms.modules.admin.vo.base.PageResultVo;
import com.tarzan.cms.modules.admin.vo.base.ResponseVo;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 后台标签配置
 *
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Controller
@RequestMapping("tag")
@AllArgsConstructor
public class TagController {

    private final TagsService tagsService;


    @GetMapping("/add")
    public String add() {
        return CoreConst.ADMIN_PREFIX + "tag/form";
    }

    @PostMapping("list")
    @ResponseBody
    public PageResultVo loadTags(Tags tags, Integer pageNumber, Integer pageSize) {
        IPage<Tags> bizTagsPage = tagsService.pageTags(tags, pageNumber, pageSize);
        return ResultUtil.table(bizTagsPage.getRecords(), bizTagsPage.getTotal());
    }

    @PostMapping("/add")
    @ResponseBody
    @CacheEvict(value = "tag", allEntries = true)
    public ResponseVo add(Tags tags) {
        Date date = new Date();
        tags.setCreateTime(date);
        tags.setUpdateTime(date);
        boolean flag = tagsService.save(tags);
        if (flag) {
            return ResultUtil.success("新增标签成功");
        } else {
            return ResultUtil.error("新增标签失败");
        }
    }

    @GetMapping("/edit")
    public String edit(Model model, Integer id) {
        Tags tags = tagsService.getById(id);
        model.addAttribute("tag", tags);
        return CoreConst.ADMIN_PREFIX + "tag/form";
    }

    @PostMapping("/edit")
    @ResponseBody
    @CacheEvict(value = "tag", allEntries = true)
    public ResponseVo edit(Tags tags) {
        tags.setUpdateTime(new Date());
        boolean flag = tagsService.updateById(tags);
        if (flag) {
            return ResultUtil.success("编辑标签成功");
        } else {
            return ResultUtil.error("编辑标签失败");
        }
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseVo delete(Integer id) {
        return deleteBatch(Arrays.asList(id));
    }

    @PostMapping("/batch/delete")
    @ResponseBody
    public ResponseVo deleteBatch(@RequestBody List<Integer> ids) {
        boolean flag = tagsService.deleteBatch(ids);
        if (flag) {
            return ResultUtil.success("删除标签成功");
        } else {
            return ResultUtil.error("删除标签失败");
        }
    }
}
