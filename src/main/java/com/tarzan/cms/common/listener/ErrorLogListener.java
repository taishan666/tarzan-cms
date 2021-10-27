package com.tarzan.cms.common.listener;

import com.tarzan.cms.common.event.ErrorLogEvent;
import com.tarzan.cms.module.admin.model.log.LogError;
import com.tarzan.cms.module.admin.service.log.LogErrorService;
import com.tarzan.cms.utils.AuthUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@AllArgsConstructor
@Component
public class ErrorLogListener {

    private LogErrorService logErrorService;

    @Async
    @Order
    @EventListener({ErrorLogEvent.class})
    public void saveErrorLog(ErrorLogEvent event) {
        LogError logError  = (LogError)event.getSource();
        logError.setCreateName(AuthUtil.getUsername());
        logError.setCreateTime(new Date());
        logErrorService.save(logError);
    }
}
