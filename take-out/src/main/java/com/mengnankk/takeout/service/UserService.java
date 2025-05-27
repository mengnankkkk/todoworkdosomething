package com.mengnankk.takeout.service;

import com.mengnankk.takeout.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 用户服务接口，提取公共用户操作逻辑
 */
public interface UserService extends UserDetailsService {
    /**
     * 根据用户名查找用户
     */
    User findByUsername(String username);
    
    /**
     * 加载用户认证信息
     */
    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
    
    /**
     * 保存用户
     */
    User saveUser(User user);
} 