package com.tarzan.cms.auth.granter;

import com.aliyun.oss.ServiceException;
import com.tarzan.cms.auth.model.UserInfo;
import com.tarzan.cms.auth.model.UserOauth;
import com.tarzan.cms.auth.provider.ITokenGranter;
import com.tarzan.cms.auth.provider.TokenParameter;
import com.tarzan.cms.auth.utils.TokenUtil;
import com.tarzan.cms.common.props.SocialProperties;
import com.tarzan.cms.modules.admin.service.sys.UserService;
import com.tarzan.cms.utils.BeanUtil;
import com.tarzan.cms.utils.Func;
import com.tarzan.cms.utils.SocialUtil;
import com.tarzan.cms.utils.WebUtil;
import lombok.AllArgsConstructor;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * SocialTokenGranter
 *
 * @author tarzan
 */
@Component
@AllArgsConstructor
public class SocialTokenGranter implements ITokenGranter {

    public static final String GRANT_TYPE = "social";

    private static final Integer AUTH_SUCCESS_CODE = 2000;

    private final UserService userService;
    private final SocialProperties socialProperties;

    @Override
    public UserInfo grant(TokenParameter tokenParameter) {
        HttpServletRequest request = WebUtil.getRequest();
        String tenantId = Func.toStr(request.getHeader(TokenUtil.TENANT_HEADER_KEY), TokenUtil.DEFAULT_TENANT_ID);
        // 开放平台来源
        String sourceParameter = request.getParameter("source");
        // 匹配是否有别名定义
        String source = socialProperties.getAlias().getOrDefault(sourceParameter, sourceParameter);
        // 开放平台授权码
        String code = request.getParameter("code");
        // 开放平台状态吗
        String state = request.getParameter("state");

        // 获取开放平台授权数据
        AuthRequest authRequest = SocialUtil.getAuthRequest(source, socialProperties);
        AuthCallback authCallback = new AuthCallback();
        authCallback.setCode(code);
        authCallback.setState(state);
        AuthResponse authResponse = authRequest.login(authCallback);
        AuthUser authUser;
        if (authResponse.getCode() == AUTH_SUCCESS_CODE) {
            authUser = (AuthUser)authResponse.getData();
        } else {
            throw new ServiceException("social grant failure, auth response is not success");
        }
        // 组装数据
        UserOauth userOauth = Objects.requireNonNull(BeanUtil.copy(authUser, UserOauth.class));
        userOauth.setSource(authUser.getSource());
        userOauth.setTenantId(tenantId);
        userOauth.setUuid(authUser.getUuid());
        // 返回UserInfo
        return userService.userInfo(userOauth);
    }

}
