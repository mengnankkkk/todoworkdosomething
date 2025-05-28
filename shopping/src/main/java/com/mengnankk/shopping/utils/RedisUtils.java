package com.mengnankk.shopping.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String REFRESH_TOKEN_KEY_PREFIX = "refresh_token:";

    /**
     * 保存刷新令牌到Redis
     */
    public void saveRefreshToken(Long userId, String refreshToken, long expireTime) {
        String key = REFRESH_TOKEN_KEY_PREFIX + userId;
        stringRedisTemplate.opsForValue().set(key, refreshToken, expireTime, TimeUnit.SECONDS);
    }

    /**
     * 从Redis获取刷新令牌
     */
    public String getRefreshToken(Long userId) {
        String key = REFRESH_TOKEN_KEY_PREFIX + userId;
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 删除刷新令牌
     */
    public void deleteRefreshToken(Long userId) {
        String key = REFRESH_TOKEN_KEY_PREFIX + userId;
        stringRedisTemplate.delete(key);
    }
} 