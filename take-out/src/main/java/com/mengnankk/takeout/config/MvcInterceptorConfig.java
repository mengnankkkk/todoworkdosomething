package com.mengnankk.takeout.config;

import com.mengnankk.takeout.interceptor.AuthInterceptor;
import com.mengnankk.takeout.interceptor.RefreshInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcInterceptorConfig implements WebMvcConfigurer {
    @Autowired
    private AuthInterceptor authInterceptor;
    @Autowired
    private RefreshInterceptor refreshInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 先注册刷新拦截器，再注册认证拦截器，保证顺序
        registry.addInterceptor(refreshInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/auth/login", "/api/auth/register");
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/auth/login", "/api/auth/register");
    }
}