package com.mengnankk.shopping.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtUtils {

    @Value("${jwt.secret:defaultSecretKey}")
    private String secret;

    @Value("${jwt.access-token-expiration:600}")
    private Long accessTokenExpiration; // 10分钟，单位秒

    @Value("${jwt.refresh-token-expiration:604800}")
    private Long refreshTokenExpiration; // 7天，单位秒

    @Value("${jwt.token-header:Authorization}")
    private String tokenHeader;

    @Value("${jwt.token-prefix:Bearer }")
    private String tokenPrefix;

    /**
     * 生成访问令牌
     */
    public String generateAccessToken(Long userId) {
        return generateToken(userId, accessTokenExpiration);
    }

    /**
     * 生成刷新令牌
     */
    public String generateRefreshToken(Long userId) {
        return generateToken(userId, refreshTokenExpiration);
    }

    /**
     * 生成令牌
     */
    private String generateToken(Long userId, Long expiration) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        Date createdDate = new Date();
        Date expirationDate = new Date(createdDate.getTime() + expiration * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 从令牌中获取用户ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims != null ? Long.valueOf(claims.get("userId").toString()) : null;
    }

    /**
     * 验证令牌是否过期
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            log.error("JWT过期检查异常", e);
            return true;
        }
    }

    /**
     * 验证令牌是否临近过期（5分钟内）
     */
    public boolean isTokenNearExpiration(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            // 5分钟 = 300秒
            return expiration.before(DateUtil.offsetSecond(new Date(), 300));
        } catch (Exception e) {
            log.error("JWT临近过期检查异常", e);
            return true;
        }
    }

    /**
     * 验证令牌
     */
    public boolean validateToken(String token) {
        if (StrUtil.isBlank(token)) {
            return false;
        }
        try {
            getClaimsFromToken(token);
            return true;
        } catch (Exception e) {
            log.error("JWT验证异常", e);
            return false;
        }
    }

    /**
     * 从令牌中获取数据声明
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 从请求头中获取JWT令牌
     */
    public String getTokenFromHeader(String header) {
        if (StrUtil.isBlank(header) || !header.startsWith(tokenPrefix)) {
            return null;
        }
        return header.substring(tokenPrefix.length());
    }
} 