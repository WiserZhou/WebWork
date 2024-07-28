package edu.hitwh.homework.entity.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

public class JwtUtils {

    // 签名密钥
    private static final String signKey = "wiser";

    /**
     * 生成JWT令牌
     *
     * @param claims JWT第二部分负载 payload 中存储的内容
     * @return 生成的JWT令牌
     */
    public static String generateJwt(Map<String, Object> claims) {
        // 有效时间
        long expire = 43200000L; // 12小时
        return Jwts.builder()
                .addClaims(claims) // 自定义信息（有效载荷）
                .signWith(SignatureAlgorithm.HS256, signKey) // 签名算法（头部）
                .setExpiration(new Date(System.currentTimeMillis() + expire)) // 过期时间
                .compact();
    }

    /**
     * 解析JWT令牌
     *
     * @param jwt JWT令牌
     */
    public static void parseJWT(String jwt) {
        Jwts.parser()
                .setSigningKey(signKey) // 指定签名密钥
                .parseClaimsJws(jwt) // 指定令牌Token
                .getBody();
    }
}
