package com.tarzan.cms.auth.model;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class TokenUser implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(
            hidden = true
    )
    private String clientId;
    private String realName;
    @ApiModelProperty(
            hidden = true
    )
    private Long userId;
    @ApiModelProperty(
            hidden = true
    )
    private String account;
    @ApiModelProperty(
            hidden = true
    )
    private String userName;
    @ApiModelProperty(
            hidden = true
    )
    private String tenantId;
    @ApiModelProperty(
            hidden = true
    )
    private String oauthId;
    @ApiModelProperty(
            hidden = true
    )
    private String roleId;
    private boolean adminFlag;

}
