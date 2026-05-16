package com.fitness.api.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // Khai báo class config cho Spring
@EnableWebSecurity // Bật Spring Security
@EnableMethodSecurity // kiểm tra quyền trên từng API
@RequiredArgsConstructor // inject filter
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // Tắt CSRF vì đây là API
                .csrf(AbstractHttpConfigurer::disable)

                // Bắt buộc dùng Stateless cho JWT
                .sessionManagement(session -> session
                        .sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS)
                )

                // Cấu hình phân quyền request
                .authorizeHttpRequests(auth -> auth

                        // Cho phép toàn bộ API đăng ký / đăng nhập không cần token
                        .requestMatchers("/api/v1/auth/**")
                        .permitAll()

                        // Cho phép cổng Webhook nhận dữ liệu tự động không cần JWT Token
                        .requestMatchers("/api/v1/payments/confirmation/webhook")
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
                )
                // Lắp máy quét JWT trước khi Spring Security kiểm tra quyền
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}