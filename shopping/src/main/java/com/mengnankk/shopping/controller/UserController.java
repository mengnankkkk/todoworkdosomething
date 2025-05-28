package com.mengnankk.shopping.controller;

import cn.hutool.core.util.StrUtil;
import com.mengnankk.shopping.common.Result;
import com.mengnankk.shopping.model.User;
import com.mengnankk.shopping.service.UserService;
import com.mengnankk.shopping.utils.JwtUtils;
import com.mengnankk.shopping.utils.RedisUtils;
import com.mengnankk.shopping.utils.UserHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "用户管理", description = "用户登录、注册、信息查询等接口")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 用户登录
     */
    @Operation(summary = "用户登录", description = "用户登录接口，登录成功后返回访问令牌和刷新令牌")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "登录成功"),
            @ApiResponse(responseCode = "500", description = "用户名或密码错误")
    })
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> loginForm) {
        String username = loginForm.get("username");
        String password = loginForm.get("password");
        
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error("用户名或密码不能为空");
        }
        
        // 调用service进行登录认证
        String accessToken = userService.login(username, password);
        
        if (StrUtil.isBlank(accessToken)) {
            return Result.error("用户名或密码错误");
        }
        
        // 获取用户ID
        Long userId = jwtUtils.getUserIdFromToken(accessToken);
        
        // 获取刷新令牌
        String refreshToken = redisUtils.getRefreshToken(userId);
        
        // 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("accessToken", accessToken);
        result.put("refreshToken", refreshToken);
        
        return Result.success("登录成功", result);
    }

    /**
     * 刷新访问令牌
     */
    @Operation(summary = "刷新访问令牌", description = "使用刷新令牌获取新的访问令牌")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "刷新成功"),
            @ApiResponse(responseCode = "401", description = "刷新令牌无效或已过期")
    })
    @PostMapping("/refresh")
    public Result<String> refreshToken(@Parameter(description = "刷新令牌", required = true) 
                                       @RequestHeader("Refresh-Token") String refreshToken) {
        if (StrUtil.isBlank(refreshToken)) {
            return Result.unauthorized("刷新令牌不能为空");
        }
        
        try {
            // 验证刷新令牌
            if (!jwtUtils.validateToken(refreshToken)) {
                return Result.unauthorized("无效的刷新令牌");
            }
            
            // 从刷新令牌中获取用户ID
            Long userId = jwtUtils.getUserIdFromToken(refreshToken);
            
            // 从Redis中获取存储的刷新令牌
            String storedRefreshToken = redisUtils.getRefreshToken(userId);
            
            // 验证刷新令牌是否匹配
            if (!refreshToken.equals(storedRefreshToken)) {
                return Result.unauthorized("刷新令牌已失效，请重新登录");
            }
            
            // 生成新的访问令牌
            String newAccessToken = jwtUtils.generateAccessToken(userId);
            
            return Result.success("刷新成功", newAccessToken);
        } catch (Exception e) {
            log.error("刷新令牌异常", e);
            return Result.unauthorized("刷新令牌异常，请重新登录");
        }
    }

    /**
     * 获取当前登录用户信息
     */
    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的详细信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "操作成功"),
            @ApiResponse(responseCode = "401", description = "未授权")
    })
    @SecurityRequirement(name = "JWT")
    @GetMapping("/info")
    public Result<User> getUserInfo() {
        // 从ThreadLocal中获取当前用户
        User user = UserHolder.get();
        // 隐藏密码
        user.setPassword(null);
        return Result.success(user);
    }

    /**
     * 登出
     */
    @Operation(summary = "用户登出", description = "用户登出，清除刷新令牌")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "登出成功")
    })
    @SecurityRequirement(name = "JWT")
    @PostMapping("/logout")
    public Result<Void> logout() {
        // 从ThreadLocal中获取用户ID
        Long userId = UserHolder.getUserId();
        // 删除Redis中的刷新令牌
        redisUtils.deleteRefreshToken(userId);
        return Result.success();
    }
} 