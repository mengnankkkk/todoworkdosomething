package com.mengnankk.shopping.service.impl;

import com.mengnankk.shopping.model.User;
import com.mengnankk.shopping.service.UserService;
import com.mengnankk.shopping.utils.JwtUtils;
import com.mengnankk.shopping.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    // 模拟用户数据库
    private static final Map<Long, User> userMap = new HashMap<>();
    
    static {
        userMap.put(1L, new User(1L, "admin", "123456", "管理员", "avatar1.jpg"));
        userMap.put(2L, new User(2L, "user", "123456", "普通用户", "avatar2.jpg"));
    }

    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private RedisUtils redisUtils;
    
    @Value("${jwt.refresh-token-expiration:604800}")
    private Long refreshTokenExpiration;

    @Override
    public User getUserById(Long userId) {
        // 从模拟数据库中获取用户
        return userMap.get(userId);
    }

    @Override
    public String login(String username, String password) {
        // 模拟用户登录校验
        for (User user : userMap.values()) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                // 生成访问令牌
                String accessToken = jwtUtils.generateAccessToken(user.getId());
                // 生成刷新令牌
                String refreshToken = jwtUtils.generateRefreshToken(user.getId());
                // 保存刷新令牌到Redis
                redisUtils.saveRefreshToken(user.getId(), refreshToken, refreshTokenExpiration);
                // 返回访问令牌
                return accessToken;
            }
        }
        return null;
    }
} 