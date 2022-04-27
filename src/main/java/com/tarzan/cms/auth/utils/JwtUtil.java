package com.tarzan.cms.auth.utils;


import com.tarzan.cms.auth.props.JwtProperties;
import com.tarzan.cms.utils.Base64Util;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class JwtUtil {
    private static final String TOKEN_CACHE = ":blade:token:state";
    private static final String BEARER = "bearer";
    public static Integer AUTH_LENGTH = 7;
    private static JwtProperties jwtProperties;
    private static RedisTemplate<String, Object> redisTemplate;

    public JwtUtil() {
    }

    public static JwtProperties getJwtProperties() {
        return jwtProperties;
    }

    public static void setJwtProperties(JwtProperties properties) {
        if (jwtProperties == null) {
            jwtProperties = properties;
        }

    }

    public static RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    public static void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        if (JwtUtil.redisTemplate == null) {
            JwtUtil.redisTemplate = redisTemplate;
        }

    }

    public static String encodeSignKey() {
        return Base64Util.encode(getJwtProperties().getSignKey());
    }

    public static String decodeSignKey(String base64) {
        return Base64Util.decode(base64);
    }

    public static String getTokenKey(String auth) {
        if (auth != null && auth.length() > AUTH_LENGTH) {
            String headStr = auth.substring(0, 6).toLowerCase();
            if ("bearer".equals(headStr)) {
                auth = auth.substring(7);
            }

            return auth;
        } else {
            return null;
        }
    }

    public static Claims parseJWT(String jsonWebToken) {
        try {
            return (Claims)Jwts.parser().setSigningKey(getJwtProperties().getSignKey().getBytes(StandardCharsets.UTF_8)).parseClaimsJws(jsonWebToken).getBody();
        } catch (Exception var2) {
            return null;
        }
    }

    public static String getAccessToken(String tenantId, String userId, String accessToken) {
        return String.valueOf(getRedisTemplate().opsForValue().get(getTokenKey(tenantId, userId, accessToken)));
    }

    public static void addToken(String tenantId, String userId, String accessToken, int expire) {
        getRedisTemplate().delete(getTokenKey(tenantId, userId, accessToken));
        getRedisTemplate().opsForValue().set(getTokenKey(tenantId, userId, accessToken), accessToken, (long)expire, TimeUnit.SECONDS);
    }

    public static void removeToken(String tenantId, String userId) {
        removeToken(tenantId, userId, (String)null);
    }

    public static void removeToken(String tenantId, String userId, String accessToken) {
        getRedisTemplate().delete(getTokenKey(tenantId, userId, accessToken));
    }

    public static String getTokenKey(String tenantId, String userId, String accessToken) {
        String key = tenantId.concat(":blade:token:state");
        return !getJwtProperties().getSingle() && !StringUtils.isEmpty(accessToken) ? key.concat(accessToken) : key.concat(userId);
    }
}
