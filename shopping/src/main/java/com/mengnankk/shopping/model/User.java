package com.mengnankk.shopping.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户信息")
public class User {
    @Schema(description = "用户ID", example = "1")
    private Long id;
    
    @Schema(description = "用户名", example = "admin")
    private String username;
    
    @Schema(description = "密码", example = "123456")
    private String password;
    
    @Schema(description = "昵称", example = "管理员")
    private String nickname;
    
    @Schema(description = "头像URL", example = "avatar1.jpg")
    private String avatar;
} 