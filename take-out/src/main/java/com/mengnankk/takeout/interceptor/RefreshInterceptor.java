package com.mengnankk.takeout.interceptor;

import com.mengnankk.takeout.entity.User;
import com.mengnankk.takeout.service.UserDetailsServiceImpl;
import com.mengnankk.takeout.utils.JwtUtil;
import com.mengnankk.takeout.utils.UserHolder;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class RefreshInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private static final String AUTH_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    // 距离过期小于5分钟时刷新
    private static final long REFRESH_THRESHOLD_MILLIS = 5 * 60 * 1000L;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authHeader = request.getHeader(AUTH_HEADER);
        if (authHeader == null || !authHeader.startsWith(TOKEN_PREFIX)) {
            return true; // 交由AuthInterceptor处理
        }
        String token = authHeader.substring(TOKEN_PREFIX.length());
        Claims claims;
        try {
            claims = jwtUtil.getAllClaimsFromToken(token);
        } catch (Exception e) {
            return true; // 交由AuthInterceptor处理
        }
        Date expiration = claims.getExpiration();
        long now = System.currentTimeMillis();
        long expMillis = expiration.getTime();
        // 如果Token即将过期
        if (expMillis - now < REFRESH_THRESHOLD_MILLIS && expMillis > now) {
            String username = claims.getSubject();
            User user = userDetailsService.findByUsername(username);
            if (user != null) {
                String newToken = jwtUtil.generateToken(username);
                response.setHeader(AUTH_HEADER, TOKEN_PREFIX + newToken);
                // 重新写入ThreadLocal
                UserHolder.set(user);
            }
        }
        return true;
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.remove();
    }
}