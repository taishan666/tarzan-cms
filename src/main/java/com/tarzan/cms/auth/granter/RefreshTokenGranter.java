package com.tarzan.cms.auth.granter;

import com.tarzan.cms.auth.constant.TokenConstant;
import com.tarzan.cms.auth.model.UserInfo;
import com.tarzan.cms.auth.provider.ITokenGranter;
import com.tarzan.cms.auth.provider.TokenParameter;
import com.tarzan.cms.auth.utils.AuthUtil;
import com.tarzan.cms.modules.admin.service.sys.UserService;
import com.tarzan.cms.utils.Func;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * RefreshTokenGranter
 *
 * @author tarzan 
 */
@Component
@AllArgsConstructor
public class RefreshTokenGranter implements ITokenGranter {

    public static final String GRANT_TYPE = "refresh_token";

    private final UserService userService;

    @Override
    public UserInfo grant(TokenParameter tokenParameter) {
        String grantType = tokenParameter.getArgs().getStr("grantType");
        String refreshToken = tokenParameter.getArgs().getStr("refreshToken");
        UserInfo userInfo = null;
        if (Func.isNoneBlank(grantType, refreshToken) && grantType.equals(TokenConstant.REFRESH_TOKEN)) {
            Claims claims = AuthUtil.parseJWT(refreshToken);
            String tokenType = Func.toStr(Objects.requireNonNull(claims).get(TokenConstant.TOKEN_TYPE));
            if (tokenType.equals(TokenConstant.REFRESH_TOKEN)) {
                // 获取用户信息
                userInfo = userService.userInfo(Func.toLong(claims.get(TokenConstant.USER_ID)));
            }
        }
        return userInfo;
    }
}
