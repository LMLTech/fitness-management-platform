package com.fitness.core.auth.port.out;

import java.util.Optional;

public interface ITokenStoragePort {
    // Lưu mã OTP / Refresh Token với thời gian sống tính bằng phút
    void saveToken(String key, String value, long durationInMinutes);

    // Lấy mã ra để kiểm tra khi user nhập vào
    Optional<String> getToken(String key);

    // Xóa mã đi ngay sau khi xác thực thành công để chống xài lại
    void deleteToken(String key);
}