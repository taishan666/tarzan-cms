package com.tarzan.cms.modules.admin.controller.biz;

import com.tarzan.cms.common.constant.CoreConst;
import com.tarzan.cms.modules.admin.model.biz.Theme;
import com.tarzan.cms.modules.admin.service.biz.ThemeService;
import com.tarzan.cms.modules.admin.vo.base.ResponseVo;
import com.tarzan.cms.utils.ResultUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
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

    private final ThemeService themeService;


    @GetMapping("/add")
    public String add() {
        return CoreConst.ADMIN_PREFIX + "theme/form";
    }

    @ResponseBody
    @PostMapping("/upload")
    public ResponseVo upload(@RequestParam(value = "file", required = false) MultipartFile file) {
        return themeService.upload(file);
    }

    @ResponseBody
    @PostMapping("download")
    public ResponseVo download(@RequestParam("url") String url) {
        return themeService.download(url);
    }

    @PostMapping("/use")
    @ResponseBody
    public ResponseVo use(Integer id) {
        boolean flag = themeService.useTheme(id);
        if (flag) {
            return ResultUtil.success("启用主题成功");
        } else {
            return ResultUtil.error("启用主题失败");
        }
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseVo delete(Integer id) {
        Theme theme=themeService.getById(id);
        if(theme==null){
            return ResultUtil.error("启用主题不存在");
        }
        if(CoreConst.STATUS_VALID.equals(theme.getStatus())){
            return ResultUtil.error("启用主题不能删除");
        }
        boolean flag = themeService.delete(id);
        if (flag) {
            return ResultUtil.success("删除主题成功");
        } else {
            return ResultUtil.error("删除主题失败");
        }
    }


}
