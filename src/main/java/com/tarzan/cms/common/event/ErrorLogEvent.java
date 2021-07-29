package com.tarzan.cms.common.event;

import com.tarzan.cms.module.admin.model.log.LogError;
import org.springframework.context.ApplicationEvent;

public class ErrorLogEvent extends ApplicationEvent {
    public ErrorLogEvent(LogError source) {
        super(source);
    }
}