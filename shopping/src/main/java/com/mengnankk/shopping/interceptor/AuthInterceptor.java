package com.mengnankk.shopping.interceptor;

import cn.hutool.core.util.StrUtil;
import com.mengnankk.shopping.model.User;
import com.mengnankk.shopping.service.UserService;
import com.mengnankk.shopping.utils.JwtUtils;
import com.mengnankk.shopping.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT认证拦截器
 */
@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从请求头中获取token
        String token = jwtUtils.getTokenFromHeader(request.getHeader("Authorization"));
        
        // 如果token为空，返回401未授权
        if (StrUtil.isBlank(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"未授权，请先登录\"}");
            return false;
        }
        
        try {
            // 验证token
            if (!jwtUtils.validateToken(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":401,\"message\":\"无效的token\"}");
                return false;
            }
            
            // 从token中获取用户ID
            Long userId = jwtUtils.getUserIdFromToken(token);
            
            // 查询用户信息
            User user = userService.getUserById(userId);
            if (user == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":401,\"message\":\"用户不存在\"}");
                return false;
            }
            
            // 将用户信息存入ThreadLocal
            UserHolder.set(user);
            return true;
        } catch (Exception e) {
            log.error("JWT认证异常", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"token验证异常\"}");
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 请求完成后，清除ThreadLocal中的用户信息，防止内存泄漏
        UserHolder.remove();
    }
} 