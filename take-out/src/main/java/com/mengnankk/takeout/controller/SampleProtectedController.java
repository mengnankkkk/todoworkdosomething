package com.mengnankk.takeout.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal; // Alternative way to get user

import com.mengnankk.takeout.entity.User;
import com.mengnankk.takeout.utils.UserHolder;

@RestController
@RequestMapping("/api/data")
public class SampleProtectedController {

    @GetMapping("/me")
    public ResponseEntity<String> getMyInfo() {
        User user = UserHolder.get();
        if (user == null) {
            return ResponseEntity.status(401).body("未登录或Token无效");
        }
        return ResponseEntity.ok("Hello, " + user.getUsername() + "! (UserId: " + user.getId() + ")");
    }

    @GetMapping("/public") // Example of a public endpoint within /api/data if needed
    public ResponseEntity<String> getPublicData() {
        return ResponseEntity.ok("This is public data, no token needed.");
    }
}