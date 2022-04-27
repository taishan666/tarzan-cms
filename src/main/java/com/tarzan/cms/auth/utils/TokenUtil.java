package com.tarzan.cms.auth.utils;

import com.tarzan.cms.auth.constant.AppConstant;
import com.tarzan.cms.auth.constant.TokenConstant;
import com.tarzan.cms.auth.model.TokenInfo;
import com.tarzan.cms.auth.model.UserInfo;
import com.tarzan.cms.auth.support.Kv;
import com.tarzan.cms.modules.admin.model.sys.User;
import com.tarzan.cms.utils.Func;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证工具类
 *
 * @author tarzan 
 */
public class TokenUtil {

    public final static String CAPTCHA_HEADER_KEY = "Captcha-Key";
    public final static String CAPTCHA_HEADER_CODE = "Captcha-Code";
    public final static String CAPTCHA_NOT_CORRECT = "验证码不正确";
    public final static String TENANT_HEADER_KEY = "Tenant-Id";
    public final static String DEFAULT_TENANT_ID = "000000";
    public final static String USER_TYPE_HEADER_KEY = "User-Type";
    public final static String DEFAULT_USER_TYPE = "web";
    public final static String USER_NOT_FOUND = "用户名或密码错误";
    public final static String USER_HAS_NO_ROLE = "未获得用户的角色信息";
    public final static String USER_HAS_NO_TENANT = "未获得用户的租户信息";
    public final static String USER_HAS_NO_TENANT_PERMISSION = "租户授权已过期,请联系管理员";
    public final static String HEADER_KEY = "Authorization";
    public final static String HEADER_PREFIX = "Basic ";
    public final static String DEFAULT_AVATAR = "https://gw.alipayobjects.com/zos/rmsportal/BiazfanxmamNRoxxVxka.png";



    /**
     * 创建认证token
     *
     * @param userInfo 用户信息
     * @return token
     */
    public static Kv createAuthInfo(UserInfo userInfo) {
        Kv authInfo = Kv.create();
        User user = userInfo.getUser();
        // 设置jwt参数
        Map<String, String> param = new HashMap<>(16);
        param.put(TokenConstant.TOKEN_TYPE, TokenConstant.ACCESS_TOKEN);
        param.put(TokenConstant.REAL_NAME, user.getRealName());
     //   param.put(TokenConstant.TENANT_ID, user.getTenantId());
        param.put(TokenConstant.USER_ID, Func.toStr(user.getId()));
        param.put(TokenConstant.OAUTH_ID, userInfo.getOauthId());
        param.put(TokenConstant.ACCOUNT, user.getUsername());
        param.put(TokenConstant.USER_NAME, user.getUsername());
        param.put(TokenConstant.NICK_NAME, user.getNickname());
        param.put(TokenConstant.REFRESH_TOKEN, user.getRealName());
        param.put(TokenConstant.ADMIN_FLAG, Func.toStr(userInfo.getAdminFlag()));
        // 拼装accessToken
        try {
            TokenInfo accessToken = SecureUtil.createJWT(param, "audience", "issuser", TokenConstant.ACCESS_TOKEN);
            // 返回accessToken
            return authInfo.set(TokenConstant.TENANT_ID, AppConstant.DEFAULT_TENANT_ID).set(TokenConstant.USER_ID, Func.toStr(user.getId())).set(TokenConstant.REAL_NAME, user.getRealName())
                .set(TokenConstant.OAUTH_ID, userInfo.getOauthId()).set(TokenConstant.ACCOUNT, user.getUsername()).set(TokenConstant.USER_NAME, user.getUsername())
                .set(TokenConstant.AVATAR, Func.toStr(user.getAvatar(), TokenConstant.DEFAULT_AVATAR)).set(TokenConstant.ACCESS_TOKEN, accessToken.getToken())
                .set(TokenConstant.REFRESH_TOKEN, createRefreshToken(userInfo).getToken()).set(TokenConstant.TOKEN_TYPE, TokenConstant.BEARER).set(TokenConstant.EXPIRES_IN, accessToken.getExpire())
                .set(TokenConstant.LICENSE, TokenConstant.LICENSE_NAME).set(TokenConstant.ADMIN_FLAG, userInfo.getAdminFlag());
        } catch (Exception ex) {
            return authInfo.set("error_code", HttpServletResponse.SC_UNAUTHORIZED).set("error_description", ex.getMessage());
        }
    }

    /**
     * 创建refreshToken
     *
     * @param userInfo 用户信息
     * @return refreshToken
     */
    private static TokenInfo createRefreshToken(UserInfo userInfo) {
        User user = userInfo.getUser();
        Map<String, String> param = new HashMap<>(16);
        param.put(TokenConstant.TOKEN_TYPE, TokenConstant.REFRESH_TOKEN);
        param.put(TokenConstant.USER_ID, Func.toStr(user.getId()));
        return SecureUtil.createJWT(param, "audience", "issuser", TokenConstant.REFRESH_TOKEN);
    }


}
