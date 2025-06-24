package com.example.ecbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Configuration
public class SecurityConfig {

    // ✅ 密码加密器
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ✅ 自定义认证入口点（理论上不会再触发，因为 login 统一用 code）
    @Bean
    public AuthenticationEntryPoint customAuthenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(HttpStatus.OK.value()); // 始终返回 200
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":1, \"message\":\"未授权，禁止访问\"}");
        };
    }

    // ✅ 安全链配置
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> {})                         // ✅ 跨域支持
            .csrf(csrf -> csrf.disable())             // ✅ 禁用 CSRF
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/**").permitAll()  // ✅ 允许所有 /api 路径
                .anyRequest().authenticated()            // 其他路径需登录（可调）
            )
            .formLogin(form -> form.disable())        // ✅ 禁用默认登录页
            .httpBasic(basic -> basic.disable());

        return http.build();
    }

    // ✅ 跨域配置（允许 Vue 前端访问）
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns("*")
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
