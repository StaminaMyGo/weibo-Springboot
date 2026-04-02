package com.wei.it.weibo.config;

import com.wei.it.weibo.web.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyWebConfig implements WebMvcConfigurer {
    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/v1/**")
        .excludePathPatterns("/api/v1/g/**","/api/v1/reg","/api/v1/index");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*") // 允许所有来源
                .allowedMethods("POST", "GET", "PUT", "DELETE", "PATCH", "OPTIONS") // 允许的方法
                .allowedHeaders("Content-Type", "Authorization") // 允许的请求头
                .allowCredentials(false) // 带Cookie时设true，但不能同时用*
                .maxAge(3600); // 预检缓存时间
    }
}
