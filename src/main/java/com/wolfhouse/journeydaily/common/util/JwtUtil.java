package com.wolfhouse.journeydaily.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * @author linexsong
 */
public class JwtUtil {
    /**
     * 指定签名算法
     */
    static SignatureAlgorithm sig = SignatureAlgorithm.HS256;


    /**
     * 创建 JWT
     *
     * @param secret    密钥
     * @param ttlMillis 过期时间
     * @param claims    负载
     * @return JWT
     */
    public static String createJwt(String secret, long ttlMillis, Map<String, Object> claims) {
        // 指定过期时间
        long ttl = System.currentTimeMillis() + ttlMillis;
        Date exp = new Date(ttl);
        var key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), sig.getJcaName());

        // 构建 JWT
        return Jwts.builder().setClaims(claims).setExpiration(exp).signWith(key).compact();
    }

    public static Claims parseToken(String token, String secret) {
        if (token == null) {
            return null;
        }
        var key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), sig.getJcaName());

        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

}
