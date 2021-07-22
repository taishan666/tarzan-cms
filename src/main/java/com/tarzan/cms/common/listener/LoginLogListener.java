package com.tarzan.cms.common.listener;

import com.tarzan.cms.common.event.LoginLogEvent;
import com.tarzan.cms.module.admin.model.log.LoginLog;
import com.tarzan.cms.module.admin.service.log.LoginLogService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 登录日志事件监听
 *
 * @author tarzan
 * @version 1.0
 * @date 2021年04月19日 09:42:36
 */
@Slf4j
@AllArgsConstructor
@Component
public class LoginLogListener {

    private LoginLogService loginLogService;

    @Async
    @Order
    @EventListener(LoginLogEvent.class)
    public void saveLoginLog(LoginLogEvent event) {
        LoginLog loginLog = (LoginLog)event.getSource();
        loginLogService.saveOrUpdate(loginLog);
    }
}
