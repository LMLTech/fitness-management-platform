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

    private final IEmailCampaignUseCase emailCampaignUseCase;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<EmailCampaign>> createDraft(@RequestBody CreateEmailCampaignDto request) {

        // Chuyển dữ liệu từ DTO sang Domain object bằng các trường thuộc tính có ảnh và chữ tách biệt
        EmailCampaign domain = EmailCampaign.builder()
                .title(request.getTitle())
                .subject(request.getSubject())
                .imageUrl(request.getImageUrl())
                .content(request.getContent())
                .targetAudience(request.getTargetAudience())
                .build();

        EmailCampaign created = emailCampaignUseCase.createDraft(domain);

        return ResponseEntity.ok(
                ApiResponse.success(created, "Lưu bản nháp Email Marketing thành công!")
        );
    }

    @PostMapping("/{id}/send")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> sendCampaign(@PathVariable UUID id) {

        emailCampaignUseCase.sendCampaign(id);

        return ResponseEntity.ok(
                ApiResponse.success(null, "Chiến dịch Email đã được phát tán thành công!")
        );
    }
}