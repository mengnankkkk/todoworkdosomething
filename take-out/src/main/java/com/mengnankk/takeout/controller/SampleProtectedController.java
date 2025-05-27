package com.mengnankk.takeout.controller;

import com.mengnankk.takeout.utils.ThreadLocalUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/data")
public class SampleProtectedController {

    @GetMapping("/me")
    public ResponseEntity<String> getMyInfo() {
        // Retrieve the username stored in ThreadLocal by the interceptor
        String username = ThreadLocalUtil.get();
        if (username != null) {
            return ResponseEntity.ok("Hello, " + username + "! This is a protected resource.");
        } else {
            // This case should ideally not be reached if the interceptor is working correctly
            // and the endpoint is properly protected.
            return ResponseEntity.status(401).body("Unauthorized access. No user info in ThreadLocal.");
        }
    }

    @GetMapping("/public") // Example of a public endpoint within /api/data if needed
    public ResponseEntity<String> getPublicData() {
        return ResponseEntity.ok("This is public data, no token needed.");
    }
}