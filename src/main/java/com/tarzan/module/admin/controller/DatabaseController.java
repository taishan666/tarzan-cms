package com.tarzan.module.admin.controller;

import com.tarzan.common.constant.CoreConst;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
public class DatabaseController {
    @GetMapping(value = "/monitoring")
    public ModelAndView databaseMonitoring() {
        return new ModelAndView(CoreConst.ADMIN_PREFIX + "database/monitoring");
    }
}
