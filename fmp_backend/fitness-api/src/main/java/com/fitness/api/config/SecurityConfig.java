package com.fitness.api.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// khai báo thư viện CORS để chuẩn bị kết nối với frontend
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

@Configuration // Khai báo class config cho Spring
@EnableWebSecurity // Bật Spring Security
@EnableMethodSecurity // kiểm tra quyền trên từng API
@RequiredArgsConstructor // inject filter
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Cấu hình bean cors
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Cho phép tất cả các domain Frontend (localhost:3000, localhost:5173...)
        configuration.setAllowedOriginPatterns(List.of("*"));
        // Cho phép các method HTTP
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        // Cho phép truyền các Header quan trọng (đặc biệt là Authorization chứa Token)
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With", "Accept"));
        // Bắt buộc set true để Frontend có thể nhận và gửi token một cách an toàn
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Áp dụng cấu hình CORS này cho toàn bộ API (/**)
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // cấu hình cors
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // Tắt CSRF vì đây là API
                .csrf(AbstractHttpConfigurer::disable)

                // Bắt buộc dùng Stateless cho JWT
                .sessionManagement(session -> session
                        .sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS)
                )

                // Cấu hình phân quyền request
                .authorizeHttpRequests(auth -> auth

                        // Cho phép toàn bộ API đăng ký / đăng nhập không cần token
                        .requestMatchers("/api/v1/auth/**").permitAll()

                        // Cho phép cổng Webhook nhận dữ liệu tự động không cần JWT Token
                        .requestMatchers("/api/v1/payments/confirmation/webhook").permitAll()

                        // Swagger UI + OpenAPI
                        .requestMatchers(
                                "/swagger-ui/**",      // giao diện swagger
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/products/**").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/plans/**").permitAll()

                        // Tất cả API khác bắt buộc phải đăng nhập có token
                        .anyRequest().authenticated()
                )
                // Lắp máy quét JWT trước khi Spring Security kiểm tra quyền
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}