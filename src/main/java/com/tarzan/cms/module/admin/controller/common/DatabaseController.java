package com.tarzan.cms.module.admin.controller.common;

import com.tarzan.cms.common.constant.CoreConst;
import com.tarzan.cms.module.admin.service.biz.CategoryService;
import com.tarzan.cms.module.admin.vo.base.ResponseVo;
import com.tarzan.cms.utils.DbBackupTools;
import com.tarzan.cms.utils.ResultUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * 后台SQL监控
 *
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
@Controller
@RequestMapping("/database")
@Slf4j
@AllArgsConstructor
public class DatabaseController {

    private static final String backPath="C:\\Users\\liuya\\Desktop\\change";

    private final CategoryService categoryService;



    /* 数据监控 */
    @GetMapping(value = "/monitoring")
    public ModelAndView databaseMonitoring() {
        return new ModelAndView(CoreConst.ADMIN_PREFIX + "database/monitoring");
    }

    /* 备份数据库 */
    @PostMapping("backupSQL")
    public ResponseVo backupSQL(){
        DbBackupTools.backSql();
        return ResultUtil.success();
    }

    @PostMapping("rollback")
    public ResponseVo rollback(@RequestParam("filename") String fileName){
         DbBackupTools.rollback(fileName);
        return ResultUtil.success();
    }
}
