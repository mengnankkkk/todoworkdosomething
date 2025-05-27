package com.mengnankk.takeout.service;

import com.mengnankk.takeout.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * UserService接口实现类
 * 通过委托给UserDetailsServiceImpl实现功能
 */
@Service
public class UserServiceImpl implements UserService {
    
    private final UserDetailsServiceImpl userDetailsService;
    
    @Autowired
    public UserServiceImpl(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    
    @Override
    public User findByUsername(String username) {
        return userDetailsService.findByUsername(username);
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDetailsService.loadUserByUsername(username);
    }
    
    @Override
    public User saveUser(User user) {
        return userDetailsService.saveUser(user);
    }
} 