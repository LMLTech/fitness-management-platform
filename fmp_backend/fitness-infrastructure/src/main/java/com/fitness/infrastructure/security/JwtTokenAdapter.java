package com.fitness.infrastructure.security;
// Import entity User để lấy thông tin user tạo token
import com.fitness.core.auth.domain.User;
// Interface port để implement theo Clean Architecture
import com.fitness.core.auth.port.out.IJwtTokenPort;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenAdapter implements IJwtTokenPort {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;

    // TẠO TOKEN
    @Override
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("fullName", user.getFullName());
        claims.put("roles", user.getRoles()); // Nhồi roles vào JWT

        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail()) // email làm subject
                .setIssuedAt(new Date()) // thời gian tạo
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // thời gian hết hạn
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    // LẤY EMAIL TỪ TOKEN
    @Override
    @SuppressWarnings("unchecked")
    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    // KIỂM TRA TOKEN CÓ HỢP LỆ KHÔNG
    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    @Override
    public java.util.List<String> getRolesFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(io.jsonwebtoken.security.Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("roles", java.util.List.class); // Lấy cục roles đã nhồi vào lúc generate
    }
}