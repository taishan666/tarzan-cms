package com.tarzan.cms.modules.admin.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Data
public class UserOnlineVo implements Serializable {

    private static final long serialVersionUID = 1L;

    //Session Id
    private String sessionId;
    //username
    private String username;
    //Session Host
    private String host;
    //Session创建时间
    private Date startTime;
    //Session最后交互时间
    private Date lastAccess;
    /*最后登录时间*/
    private Date lastLoginTime;
    //Session timeout
    private long timeout;
    //session 是否踢出
    private boolean sessionStatus = Boolean.TRUE;

}
