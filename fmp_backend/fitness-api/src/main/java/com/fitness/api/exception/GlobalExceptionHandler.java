package com.fitness.api.exception;

import com.fitness.common.response.ApiResponse;
import com.fitness.core.common.exception.DomainException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice // Bắt lỗi toàn cục cho tất cả Controller
@Slf4j               // Tạo biến log để ghi lỗi ra console
public class GlobalExceptionHandler {

    // Lỗi nghiệp vụ tự định nghĩa...
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiResponse<Object>> handleDomainException(DomainException e) {
        log.error("Domain Error: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST) // 400
                .body(ApiResponse.error(e.getErrorCode(), e.getMessage()));
    }

    // Lỗi validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(MethodArgumentNotValidException e) {
        String details = e.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST) // 400
                .body(ApiResponse.error("VALIDATION_ERROR", "Dữ liệu không hợp lệ: " + details));
    }

    // Lỗi hệ thống
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleSystemException(Exception e) {
        log.error("System Error: ", e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR) // 500
                .body(ApiResponse.error("INTERNAL_SERVER_ERROR", "Lỗi hệ thống, vui lòng liên hệ Admin."));
    }
}