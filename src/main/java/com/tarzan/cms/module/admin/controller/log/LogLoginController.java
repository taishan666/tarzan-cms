package com.tarzan.cms.module.admin.controller.log;

import com.tarzan.cms.module.admin.service.log.LoginLogService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 登录日志
 *
 * @version 1.0
 * @since JDK1.8
 * @author tarzan
 * @date 2021年7月20日 10:24:07
 */
@RestController
@AllArgsConstructor
@RequestMapping("/login")
public class LogLoginController {

    private final LoginLogService loginLogService;

}
