package com.vain.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;


public class TokenUtils {


    /**
     * http请求头所需要的字段
     */
    private static final String header = "Token";

    /**
     * jwt秘钥
     */
    private static final String secret = "defaultSecret";

    /**
     * #30分 单位:秒
     */
    public static final Long expiration = 1800L;

    /**
     * subject
     */
    public static final String subject = "vain";

    /**
     * 认证请求的路径
     */
    private static final String authPath = "auth";

    /**
     * md5加密混淆key
     */
    private static final String md5Key = "randomKey";


    /**
     * 获取jwt的payload部分
     */
    public static Claims getClaimFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 验证token是否失效
     */
    public static Boolean isTokenExpired(String token) {
        try {
            final Date expiration = getExpirationDateFromToken(token);
            return null != expiration && expiration.before(new Date());
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    private static Date getExpirationDateFromToken(String token) {
        Claims claims = null;
        try {
            claims = getClaimFromToken(token);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return claims == null ? null : claims.getExpiration();
    }

    /**
     * 生成token
     */
    public static String generateToken(Map<String, Object> claims, String subject) {
        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + expiration * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

}
