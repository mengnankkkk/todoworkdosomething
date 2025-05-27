package com.mengnankk.takeout.service;


import com.mengnankk.takeout.entity.User;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    // In-memory user store for demonstration. Replace with a database and UserRepository in a real app.
    private static final Map<String, User> users = new HashMap<>();
    private final PasswordEncoder passwordEncoder;

    public UserDetailsServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    // 初始化用户数据可放到@PostConstruct方法
    @PostConstruct
    public void initUsers() {
        users.put("user1", new User(1L, "user1", passwordEncoder.encode("password123")));
        users.put("admin", new User(2L, "admin", passwordEncoder.encode("adminpass")));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = users.get(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        // Spring Security's User object. The third argument is for authorities/roles.
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }

    // Method to add a new user (e.g., for registration)
    // This is a simplified example. In a real app, this would interact with a UserRepository.
    public User saveUser(User user) {
        // Ensure password is encoded before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        users.put(user.getUsername(), user);
        return user;
    }

    public User findByUsername(String username) {
        return users.get(username);
    }
}