package com.tarzan.cms.auth.utils;


import com.tarzan.cms.auth.dto.ClientInfo;
import com.tarzan.cms.auth.model.TokenInfo;
import com.tarzan.cms.auth.props.JwtProperties;
import com.tarzan.cms.auth.provider.IClientDetails;
import com.tarzan.cms.auth.provider.IClientDetailsService;
import com.tarzan.cms.common.exception.SecureException;
import com.tarzan.cms.utils.*;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class SecureUtil extends AuthUtil {
    private static final String CLIENT_ID = "client_id";
    private static final IClientDetailsService clientDetailsService = SpringUtil.getBean(IClientDetailsService.class);
    private static final JwtProperties jwtProperties = SpringUtil.getBean(JwtProperties.class);

    public SecureUtil() {
    }

    private static IClientDetailsService getClientDetailsService() {
        return clientDetailsService;
    }

    private static JwtProperties getJwtProperties() {
        return jwtProperties;
    }

    public static TokenInfo createJWT(Map<String, String> user, String audience, String issuer, String tokenType) {
        ClientInfo clientInfo = clientInfo();

        assert clientInfo.valid();

        String clientId = clientInfo.getClientId();
        String clientSecret = clientInfo.getClientSecret();
        IClientDetails clientDetails = clientDetails(clientId);
        if (!validateClient(clientDetails, clientId, clientSecret)) {
            throw new SecureException("客户端认证失败，请检查请求头配置");
        } else {
            SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
            long nowMillis = System.currentTimeMillis();
            Date now = new Date(nowMillis);
            byte[] apiKeySecretBytes = JwtUtil.getJwtProperties().getSignKey().getBytes(Charsets.UTF_8);
            Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
            JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JsonWebToken").setIssuer(issuer).setAudience(audience).signWith(signatureAlgorithm, signingKey);
            user.forEach(builder::claim);
            builder.claim("client_id", clientId);
            long expireMillis;
            if (tokenType.equals("access_token")) {
                expireMillis = clientDetails.getAccessTokenValidity() * 1000;
            } else if (tokenType.equals("refresh_token")) {
                expireMillis = clientDetails.getRefreshTokenValidity() * 1000;
            } else {
                expireMillis = getExpire();
            }

            long expMillis = nowMillis + expireMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp).setNotBefore(now);
            TokenInfo tokenInfo = new TokenInfo();
            tokenInfo.setToken(builder.compact());
            tokenInfo.setExpire((int)expireMillis / 1000);
            if (getJwtProperties().getState() && "access_token".equals(tokenType)) {
                String tenantId = user.get("tenant_id");
                String userId = user.get("user_id");
                JwtUtil.addToken(tenantId, userId, tokenInfo.getToken(), tokenInfo.getExpire());
            }

            return tokenInfo;
        }
    }

    public static long getExpire() {
        Calendar cal = Calendar.getInstance();
        cal.add(6, 1);
        cal.set(11, 3);
        cal.set(13, 0);
        cal.set(12, 0);
        cal.set(14, 0);
        return cal.getTimeInMillis() - System.currentTimeMillis();
    }

    public static ClientInfo clientInfo() {
        try {
            String header = Func.toStr(WebUtil.getRequest().getHeader("Authorization"));
            header = header.replace("Basic%20", "Basic ");
            if (!header.startsWith("Basic ")) {
                throw new RuntimeException("非法的客户端，请检查请求头");
            } else {
                String decoded = Base64Util.decode(header.substring(6));
                String[] temp = decoded.split(":");
                if (!Func.isEmpty(temp) && temp.length == 2) {
                    return ClientInfo.builder().clientId(temp[0]).clientSecret(temp[1]).build();
                } else {
                    throw new RuntimeException("非法的客户端，请检查请求头");
                }
            }
        } catch (Throwable var3) {
            throw var3;
        }
    }

    public static String getClientIdFromHeader() {
        ClientInfo clientInfo = clientInfo();
        return clientInfo.getClientId();
    }

    private static IClientDetails clientDetails(String clientId) {
        return getClientDetailsService().loadClientByClientId(clientId);
    }

    private static boolean validateClient(IClientDetails clientDetails, String clientId, String clientSecret) {
        if (clientDetails == null) {
            return false;
        } else {
            return StringUtil.equals(clientId, clientDetails.getClientId()) && StringUtil.equals(clientSecret, clientDetails.getClientSecret());
        }
    }
}
