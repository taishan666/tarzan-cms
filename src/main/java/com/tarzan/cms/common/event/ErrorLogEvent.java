package com.tarzan.cms.common.event;

import com.tarzan.cms.modules.admin.model.log.LogError;
import org.springframework.context.ApplicationEvent;

/**
 * @author tarzan
 */
public class ErrorLogEvent extends ApplicationEvent {
    public ErrorLogEvent(LogError source) {
        super(source);
    }
}
