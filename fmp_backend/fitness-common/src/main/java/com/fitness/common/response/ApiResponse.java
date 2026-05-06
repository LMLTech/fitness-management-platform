package com.fitness.common.response;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // giúp loại bỏ các trường null khi trả về JSON, làm API gọn sạch hơn
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private String errorCode; // Thêm mã lỗi để Frontend dễ xử lý logic
    private LocalDateTime timestamp; // Thời điểm phản hồi


    // CÁC HELPER METHODS ĐỂ GỌI NHANH
    // Success có data + message
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // Success có data
    public static <T> ApiResponse<T> success(T data) {
        return success(data, "Success");
    }

    // Success không có data
    public static <T> ApiResponse<T> success(String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // Error cơ bản
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // Error có mã lỗi chi tiết
    public static <T> ApiResponse<T> error(String errorCode, String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .errorCode(errorCode)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}