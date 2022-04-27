package com.tarzan.cms.auth.provider;

public interface IClientDetailsService {
    IClientDetails loadClientByClientId(String clientId);
}
