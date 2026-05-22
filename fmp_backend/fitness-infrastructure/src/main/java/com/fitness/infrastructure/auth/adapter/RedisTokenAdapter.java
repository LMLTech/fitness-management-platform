package com.fitness.infrastructure.auth.adapter;

import com.fitness.core.auth.port.out.ITokenStoragePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisTokenAdapter implements ITokenStoragePort {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void saveToken(String key, String value, long durationInMinutes) {
        // Ma thuật của Redis nằm ở đây: Lưu dữ liệu kèm theo đồng hồ đếm ngược tự hủy
        redisTemplate.opsForValue().set(key, value, durationInMinutes, TimeUnit.MINUTES);
    }

    @Override
    public Optional<String> getToken(String key) {
        Object token = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(token != null ? token.toString() : null);
    }

    @Override
    public void deleteToken(String key) {
        redisTemplate.delete(key);
    }
}