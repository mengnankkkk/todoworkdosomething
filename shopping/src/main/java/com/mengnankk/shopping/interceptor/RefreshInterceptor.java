package com.mengnankk.shopping.interceptor;

import cn.hutool.core.util.StrUtil;
import com.mengnankk.shopping.utils.JwtUtils;
import com.mengnankk.shopping.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Token刷新拦截器
 */
@Slf4j
@Component
public class RefreshInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从请求头中获取token
        String token = jwtUtils.getTokenFromHeader(request.getHeader("Authorization"));
        
        // 如果token为空，直接放行，交给后续AuthInterceptor处理
        if (StrUtil.isBlank(token)) {
            return true;
        }
        
        try {
            // 判断token是否临近过期（5分钟内）
            if (jwtUtils.isTokenNearExpiration(token) && jwtUtils.validateToken(token)) {
                // 从token中获取用户ID
                Long userId = jwtUtils.getUserIdFromToken(token);
                
                // 从Redis获取刷新令牌
                String refreshToken = redisUtils.getRefreshToken(userId);
                
                // 如果刷新令牌存在
                if (StrUtil.isNotBlank(refreshToken)) {
                    // 生成新的访问令牌
                    String newAccessToken = jwtUtils.generateAccessToken(userId);
                    
                    // 将新的访问令牌返回给前端
                    response.setHeader("Authorization", "Bearer " + newAccessToken);
                    response.setHeader("Access-Control-Expose-Headers", "Authorization");
                    
                    log.info("用户{}的token已刷新", userId);
                }
            }
            return true;
        } catch (Exception e) {
            log.error("Token刷新异常", e);
            return true; // 即使刷新出错，也放行请求，交给后续AuthInterceptor处理
        }
    }
} 