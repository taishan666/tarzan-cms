package com.tarzan.cms.auth.model;

import com.tarzan.cms.modules.admin.model.sys.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户信息
 *
 * @version 1.0
 * @since JDK1.8
 * @author tarzan
 */
@Data
@ApiModel(description = "用户信息")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户基础信息
     */
    @ApiModelProperty(value = "用户")
    private User user;

    /**
     * 第三方授权id
     */
    @ApiModelProperty(value = "第三方授权id")
    private String oauthId;


    private Boolean adminFlag;

}
