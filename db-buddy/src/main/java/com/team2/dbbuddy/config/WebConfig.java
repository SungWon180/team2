package com.team2.dbbuddy.config;

import com.team2.dbbuddy.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**") // Apply to all paths by default
                .excludePathPatterns(
                        "/user/login",
                        "/user/signup",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/error"); // Exclude login/signup and static resources
    }
}
