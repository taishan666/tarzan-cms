package com.tarzan.cms.auth.provider;


import java.io.Serializable;

public interface IClientDetails extends Serializable {
    String getClientId();

    String getClientSecret();

    Integer getAccessTokenValidity();

    Integer getRefreshTokenValidity();
}
