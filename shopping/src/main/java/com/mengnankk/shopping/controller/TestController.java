package com.mengnankk.shopping.controller;

import com.mengnankk.shopping.common.Result;
import com.mengnankk.shopping.utils.UserHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试控制器
 */
@RestController
@RequestMapping("/test")
@Tag(name = "测试接口", description = "用于测试JWT认证的接口")
public class TestController {

    /**
     * 测试接口
     */
    @Operation(summary = "问候接口", description = "返回带有用户昵称的问候语")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "操作成功"),
            @ApiResponse(responseCode = "401", description = "未授权")
    })
    @SecurityRequirement(name = "JWT")
    @GetMapping("/hello")
    public Result<String> hello() {
        return Result.success("你好，" + UserHolder.get().getNickname());
    }
} 