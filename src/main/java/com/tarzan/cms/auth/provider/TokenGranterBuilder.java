package com.tarzan.cms.auth.provider;

import com.tarzan.cms.auth.granter.CaptchaTokenGranter;
import com.tarzan.cms.auth.granter.PasswordTokenGranter;
import com.tarzan.cms.auth.granter.RefreshTokenGranter;
import com.tarzan.cms.auth.granter.SocialTokenGranter;
import com.tarzan.cms.common.exception.SecureException;
import com.tarzan.cms.utils.Func;
import com.tarzan.cms.utils.SpringUtil;
import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TokenGranterBuilder
 *
 * @author tarzan 
 */
@AllArgsConstructor
public class TokenGranterBuilder {

    /**
     * TokenGranter缓存池
     */
    private static final Map<String, ITokenGranter> GRANTER_POOL = new ConcurrentHashMap<>();

    static {
        GRANTER_POOL.put(PasswordTokenGranter.GRANT_TYPE, SpringUtil.getBean(PasswordTokenGranter.class));
        GRANTER_POOL.put(CaptchaTokenGranter.GRANT_TYPE, SpringUtil.getBean(CaptchaTokenGranter.class));
        GRANTER_POOL.put(RefreshTokenGranter.GRANT_TYPE, SpringUtil.getBean(RefreshTokenGranter.class));
        GRANTER_POOL.put(SocialTokenGranter.GRANT_TYPE, SpringUtil.getBean(SocialTokenGranter.class));
    }

    /**
     * 获取TokenGranter
     *
     * @param grantType 授权类型
     * @return ITokenGranter
     */
    public static ITokenGranter getGranter(String grantType) {
        ITokenGranter tokenGranter = GRANTER_POOL.get(Func.toStr(grantType, PasswordTokenGranter.GRANT_TYPE));
        if (tokenGranter == null) {
            throw new SecureException("no grantType was found");
        } else {
            return GRANTER_POOL.get(Func.toStr(grantType, PasswordTokenGranter.GRANT_TYPE));
        }
    }

}
