package com.tarzan.cms.auth.provider;

import com.tarzan.cms.auth.model.UserInfo;

/**
 * 授权认证统一接口.
 *
 * @author tarzan 
 */
public interface ITokenGranter {

    /**
     * 获取用户信息
     *
     * @param tokenParameter 授权参数
     * @return UserInfo
     */
    UserInfo grant(TokenParameter tokenParameter);

}
