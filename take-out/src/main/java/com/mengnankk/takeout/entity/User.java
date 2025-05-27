package com.mengnankk.takeout.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String username;
    private String password; // In a real app, this should be hashed
    // Add other user-related fields like email, roles, etc.
}