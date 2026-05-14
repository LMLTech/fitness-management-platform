package com.fitness.api.auth.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class CreateLeaveRequestDto {

    // Ngày bắt đầu nghỉ phép
    private LocalDate startDate;

    // Ngày kết thúc nghỉ phép
    private LocalDate endDate;

    // Lý do xin nghỉ
    private String reason;
}