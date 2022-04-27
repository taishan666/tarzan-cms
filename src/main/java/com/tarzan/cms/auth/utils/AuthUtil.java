package com.tarzan.cms.auth.utils;


import com.tarzan.cms.auth.constant.AppConstant;
import com.tarzan.cms.auth.model.TokenUser;
import com.tarzan.cms.auth.props.JwtProperties;
import com.tarzan.cms.utils.Func;
import com.tarzan.cms.utils.SpringUtil;
import com.tarzan.cms.utils.StringUtil;
import com.tarzan.cms.utils.WebUtil;
import io.jsonwebtoken.Claims;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class AuthUtil {
    private static final String REAL_NAME = "real_name";
    private static final String BLADE_USER_REQUEST_ATTR = "_BLADE_USER_REQUEST_ATTR_";
    private static final String HEADER = "Blade-Auth";
    private static final String ACCOUNT = "account";
    private static final String USER_NAME = "user_name";
    private static final String USER_ID = "user_id";
    private static final String ROLE_ID = "role_id";
    private static final String TENANT_ID = "tenant_id";
    private static final String OAUTH_ID = "oauth_id";
    private static final String CLIENT_ID = "client_id";
    private static final String ADMIN_FLAG = "admin_flag";
    private static JwtProperties jwtProperties;

    public AuthUtil() {
    }

    private static JwtProperties getJwtProperties() {
        if (jwtProperties == null) {
            jwtProperties = SpringUtil.getBean(JwtProperties.class);
        }

        return jwtProperties;
    }

    public static TokenUser getUser() {
        HttpServletRequest request = WebUtil.getRequest();
        if (request == null) {
            return null;
        } else {
            Object TokenUser = request.getAttribute("_BLADE_USER_REQUEST_ATTR_");
            if (TokenUser == null) {
                TokenUser = getUser(request);
                if (TokenUser != null) {
                    request.setAttribute("_BLADE_USER_REQUEST_ATTR_", TokenUser);
                }
            }

            return (TokenUser)TokenUser;
        }
    }

    public static TokenUser getUser(String token) {
        Claims claims = parseJWT(token);
        return cliamsToUser(claims);
    }

    public static TokenUser getUser(HttpServletRequest request) {
        Claims claims = getClaims(request);
        if (claims == null) {
            return null;
        } else {
            return cliamsToUser(claims);
        }
    }

    private static TokenUser cliamsToUser(Claims claims) {
        if (Objects.isNull(claims)) {
            return null;
        } else {
            TokenUser TokenUser = new TokenUser();
            TokenUser.setClientId(Func.toStr(claims.get("client_id")));
            TokenUser.setUserId(Func.toLong(claims.get("user_id")));
            TokenUser.setTenantId(Func.toStr(claims.get("tenant_id")));
            TokenUser.setOauthId(Func.toStr(claims.get("oauth_id")));
            TokenUser.setRoleId(Func.toStrWithEmpty(claims.get("role_id"), "-1"));
            TokenUser.setAccount(Func.toStr(claims.get("account")));
            TokenUser.setUserName(Func.toStr(claims.get("user_name")));
            TokenUser.setRealName(Func.toStr(claims.get("real_name")));
            TokenUser.setAdminFlag(Func.toBoolean(claims.get("admin_flag"), Boolean.FALSE));
            return TokenUser;
        }
    }

    public static boolean isAdministrator() {
        TokenUser user = getUser();
        return user != null && user.isAdminFlag();
    }

    public static Long getUserId() {
        TokenUser user = getUser();
        return null == user ? -1L : user.getUserId();
    }

    public static Long getUserId(HttpServletRequest request) {
        TokenUser user = getUser(request);
        return null == user ? -1L : user.getUserId();
    }

    public static String getRealName(HttpServletRequest request) {
        TokenUser user = getUser(request);
        return null == user ? "unknown" : user.getRealName();
    }

    public static String getRealName() {
        TokenUser user = getUser();
        return null == user ? "unknown" : user.getRealName();
    }

    public static String getUserAccount() {
        TokenUser user = getUser();
        return null == user ? "" : user.getAccount();
    }

    public static String getUserAccount(HttpServletRequest request) {
        TokenUser user = getUser(request);
        return null == user ? "" : user.getAccount();
    }

    public static String getUserName() {
        TokenUser user = getUser();
        return null == user ? "" : user.getUserName();
    }

    public static String getUserName(HttpServletRequest request) {
        TokenUser user = getUser(request);
        return null == user ? "" : user.getUserName();
    }

    public static String getUserRole() {
        TokenUser user = getUser();
        return null == user ? "" : user.getRoleId();
    }

    public static String getUserRole(HttpServletRequest request) {
        TokenUser user = getUser(request);
        return null == user ? "" : user.getRoleId();
    }

    public static String getTenantId() {
        TokenUser user = getUser();
        return null == user ? "" : user.getTenantId();
    }

    public static String getTenantId(HttpServletRequest request) {
        TokenUser user = getUser(request);
        return null == user ? "" : user.getTenantId();
    }

    public static String getOauthId() {
        TokenUser user = getUser();
        return null == user ? "" : user.getOauthId();
    }

    public static String getOauthId(HttpServletRequest request) {
        TokenUser user = getUser(request);
        return null == user ? "" : user.getOauthId();
    }

    public static String getClientId() {
        TokenUser user = getUser();
        return null == user ? "" : user.getClientId();
    }

    public static String getClientId(HttpServletRequest request) {
        TokenUser user = getUser(request);
        return null == user ? "" : user.getClientId();
    }

    public static String getToken() {
        HttpServletRequest request = WebUtil.getRequest();
        String auth = request.getHeader("Blade-Auth");
        return JwtUtil.getTokenKey(auth);
    }

    public static TokenUser defaultUser() {
        TokenUser defaultUser = new TokenUser();
        defaultUser.setUserId(AppConstant.DEFAULT_USER_ID);
        defaultUser.setTenantId("000000");
        defaultUser.setRealName("默认用户");
        return defaultUser;
    }

    public static Claims getClaims(HttpServletRequest request) {
        String auth = request.getHeader("Blade-Auth");
        Claims claims = null;
        String token;
        String tenantId;
        if (StringUtil.isNotBlank(auth)) {
            token = JwtUtil.getTokenKey(auth);
        } else {
            tenantId = request.getParameter("Blade-Auth");
            token = JwtUtil.getTokenKey(tenantId);
        }

        if (StringUtil.isNotBlank(token)) {
            claims = parseJWT(token);
        }

        if (!ObjectUtils.isEmpty(claims) && getJwtProperties().getState()) {
            tenantId = Func.toStr(claims.get("tenant_id"));
            String userId = Func.toStr(claims.get("user_id"));
            String accessToken = JwtUtil.getAccessToken(tenantId, userId, token);
            if (!token.equalsIgnoreCase(accessToken)) {
                return null;
            }
        }

        return claims;
    }

    public static String getHeader() {
        return getHeader(Objects.requireNonNull(WebUtil.getRequest()));
    }

    public static String getHeader(HttpServletRequest request) {
        return request.getHeader("Blade-Auth");
    }

    public static Claims parseJWT(String jsonWebToken) {
        return JwtUtil.parseJWT(jsonWebToken);
    }
}
