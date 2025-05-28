package com.mengnankk.shopping.service;

import com.mengnankk.shopping.model.User;

public interface UserService {
    /**
     * 根据用户ID查询用户信息
     */
    User getUserById(Long userId);
    
    /**
     * 用户登录
     */
    String login(String username, String password);
} 