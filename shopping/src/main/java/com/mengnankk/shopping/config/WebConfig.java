package com.mengnankk.shopping.config;

import com.mengnankk.shopping.interceptor.AuthInterceptor;
import com.mengnankk.shopping.interceptor.RefreshInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private RefreshInterceptor refreshInterceptor;
    
    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册Token刷新拦截器（先执行）
        registry.addInterceptor(refreshInterceptor)
                .addPathPatterns("/**") // 拦截所有请求
                .excludePathPatterns("/user/login", "/user/refresh") // 排除登录和刷新token接口
                .excludePathPatterns("/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**"); // 排除Swagger相关路径
        
        // 注册JWT认证拦截器（后执行）
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**") // 拦截所有请求
                .excludePathPatterns("/user/login", "/user/refresh") // 排除登录和刷新token接口
                .excludePathPatterns("/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**"); // 排除Swagger相关路径
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置Swagger静态资源访问
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springdoc-openapi-ui/")
                .resourceChain(false);
    }
} 