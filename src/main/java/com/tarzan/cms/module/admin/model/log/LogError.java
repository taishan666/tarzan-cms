package com.tarzan.cms.module.admin.model.log;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("sys_log_error")
@EqualsAndHashCode
public class LogError implements Serializable {

    protected Long id;
    protected String remoteIp;
    protected String userAgent;
    protected String requestUri;
    protected String method;
    protected String methodClass;
    protected String methodName;
    protected String params;
    private String stackTrace;
    private String exceptionName;
    private String message;
    private String fileName;
    private Integer lineNumber;
    protected String createName;
    protected Date createTime;
}
