package com.fitness.core.auth.port.out;

public interface ITwoFactorPort{
    String generateSecretKey();
    String getQrCodeUrl(String secret, String email);
    boolean verifyCode(String secret, int code);
}