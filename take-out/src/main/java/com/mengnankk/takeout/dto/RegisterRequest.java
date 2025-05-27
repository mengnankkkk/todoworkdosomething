package com.mengnankk.takeout.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    // Add other fields like email if needed
}