package com.fitness.api.config;

import com.fitness.core.auth.port.out.IJwtTokenPort;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final IJwtTokenPort jwtTokenPort;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Lấy chuỗi "Authorization" từ Header
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            // Kiểm tra Token có đúng không
            if (jwtTokenPort.validateToken(token)) {
                String email = jwtTokenPort.getEmailFromToken(token);

                // Bóc roles từ token và chuyển thành Authority của Spring
                java.util.List<String> roles = jwtTokenPort.getRolesFromToken(token);
                java.util.List<org.springframework.security.core.authority.SimpleGrantedAuthority> authorities =
                        roles != null ? roles.stream()
                                        .map(org.springframework.security.core.authority.SimpleGrantedAuthority::new)
                                        .toList() : java.util.Collections.emptyList();

                // Truyền authorities vào đây thay vì emptyList
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        email, null, authorities
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Cho phép request đi tiếp
        filterChain.doFilter(request, response);
    }
}