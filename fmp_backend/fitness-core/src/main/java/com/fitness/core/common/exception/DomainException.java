package com.fitness.core.common.exception;

import lombok.Getter;

/**
 * Exception dùng chung cho tầng nghiệp vụ Core
 * Ví dụ: Trùng email hay gói tập hết hạn... sẽ ném ra lỗi này
 */
@Getter
public class DomainException extends RuntimeException {
    private final String errorCode;

    public DomainException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}