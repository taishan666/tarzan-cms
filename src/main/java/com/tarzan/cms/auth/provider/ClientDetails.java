package com.tarzan.cms.auth.provider;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ClientDetails implements IClientDetails {
    @ApiModelProperty("客户端id")
    private String clientId;
    @ApiModelProperty("客户端密钥")
    private String clientSecret;
    @ApiModelProperty("令牌过期秒数")
    private Integer accessTokenValidity;
    @ApiModelProperty("刷新令牌过期秒数")
    private Integer refreshTokenValidity;
}