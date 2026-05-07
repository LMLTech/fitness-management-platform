package com.fitness.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Khai báo class config cho Spring
@EnableWebSecurity // Bật Spring Security
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // Tắt CSRF vì đây là API
                .csrf(AbstractHttpConfigurer::disable)

                // Cấu hình phân quyền request
                .authorizeHttpRequests(auth -> auth

                        // Cho phép toàn bộ API đăng ký / đăng nhập không cần token
                        .requestMatchers("/v1/auth/**")
                        .permitAll()

                        // Swagger UI + OpenAPI
                        .requestMatchers(
                                "/swagger-ui/**",      // giao diện swagger
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        )
                        .permitAll()

                        // Tất cả API khác bắt buộc phải đăng nhập có token
                        .anyRequest()
                        .authenticated()
                );
        return http.build();
    }
}