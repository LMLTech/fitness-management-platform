package com.fitness.core.auth.port.out;

public interface IPasswordEncoderPort {
    String encode(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}