package com.mengnankk.takeout.config;

import com.mengnankk.takeout.interceptor.AuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AuthenticationInterceptor authenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns("/api/**") // Apply interceptor to all paths under /api/
                .excludePathPatterns("/api/auth/login", "/api/auth/register"); // Exclude login and register endpoints
        // Add more excludePathPatterns for public endpoints like swagger, static resources etc.
    }
}