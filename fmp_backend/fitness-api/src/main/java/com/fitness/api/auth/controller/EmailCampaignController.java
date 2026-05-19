package com.fitness.api.auth.controller;

import com.fitness.api.auth.dto.CreateEmailCampaignDto;
import com.fitness.common.response.ApiResponse;
import com.fitness.core.auth.domain.EmailCampaign;
import com.fitness.core.auth.port.in.IEmailCampaignUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/marketing/email-campaigns")
@RequiredArgsConstructor
public class EmailCampaignController {

    // UseCase xử lý nghiệp vụ Email Marketing
    private final IEmailCampaignUseCase emailCampaignUseCase;

    // API tạo bản nháp chiến dịch Email Marketing
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<EmailCampaign>> createDraft(@RequestBody CreateEmailCampaignDto request) {

        // Chuyển dữ liệu từ DTO sang Domain object
        EmailCampaign domain = EmailCampaign.builder()
                .title(request.getTitle())
                .subject(request.getSubject())
                .htmlContent(request.getHtmlContent())
                .targetAudience(request.getTargetAudience())
                .build();

        // Lưu bản nháp chiến dịch email
        EmailCampaign created = emailCampaignUseCase.createDraft(domain);

        return ResponseEntity.ok(
                ApiResponse.success(created, "Lưu bản nháp Email Marketing thành công!")
        );
    }

    // API gửi chiến dịch Email đến nhóm khách hàng mục tiêu
    @PostMapping("/{id}/send")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> sendCampaign(@PathVariable UUID id) {

        // Kích hoạt gửi email hàng loạt theo campaign ID
        emailCampaignUseCase.sendCampaign(id);

        return ResponseEntity.ok(
                ApiResponse.success(null, "Chiến dịch Email đã được phát tán thành công!")
        );
    }
}