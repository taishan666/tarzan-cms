package com.tarzan.cms.security.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author codermy
 * @createTime 2020/7/23
 */
@Component
public class JwtUtils {
    private String secret="tarzan";
    private  Long expiration=360000L;


    /**
     * 创建token
     * @param username 用户名
     * @return
     */
    public  String generateToken(String username) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, secret)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .compact();

    }


    /**
     * 从token中获取用户名
     * @param token
     * @return
     */
    public  String getUserNameFromToken(String token){
        return getTokenBody(token).getSubject();
    }



    /**
     *  是否已过期
     * @param token
     * @return
     */
    public  boolean isExpiration(String token){
        return getTokenBody(token).getExpiration().before(new Date());
    }

    private  Claims getTokenBody(String token){
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

}

