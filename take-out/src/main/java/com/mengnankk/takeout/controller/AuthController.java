package com.mengnankk.takeout.controller;

import com.mengnankk.takeout.dto.LoginRequest;
import com.mengnankk.takeout.dto.LoginResponse;
import com.mengnankk.takeout.entity.User;
import com.mengnankk.takeout.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    // In-memory user store for demonstration. Replace with a database in a real app.
    private static final Map<String, User> users = new HashMap<>();

    static {
        // Sample user. In a real app, passwords should be hashed.
        users.put("user1", new User(1L, "user1", "password123"));
        users.put("admin", new User(2L, "admin", "adminpass"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        User user = users.get(loginRequest.getUsername());

        // Basic authentication logic. Replace with proper password checking (e.g., Spring Security with PasswordEncoder)
        if (user != null && user.getPassword().equals(loginRequest.getPassword())) {
            String token = jwtUtil.generateToken(user.getUsername());
            return ResponseEntity.ok(new LoginResponse(token));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    // Add a register endpoint here in a real application
    // @PostMapping("/register")
    // public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) { ... }
}