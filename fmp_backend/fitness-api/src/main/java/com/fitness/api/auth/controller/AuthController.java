package com.fitness.api.auth.controller;

import com.fitness.api.auth.dto.LoginRequest;
import com.fitness.api.auth.dto.RegisterRequest;
import com.fitness.api.auth.dto.RegistrationResponse;
import com.fitness.common.response.ApiResponse;
import com.fitness.core.auth.domain.AuthResult;
import com.fitness.core.auth.port.in.IAuthUseCase;
import com.fitness.core.auth.port.in.LoginCommand;
import com.fitness.core.auth.port.in.RegisterCommand;
import com.fitness.core.auth.port.in.SocialLoginCommand;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final IAuthUseCase authUseCase;

    public AuthController(IAuthUseCase authUseCase) {
        this.authUseCase = authUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegistrationResponse>> register(@Valid @RequestBody RegisterRequest request) {
        RegisterCommand command = RegisterCommand.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .rawPassword(request.getPassword())
                .fullName(request.getFullName())
                .build();

        AuthResult result = authUseCase.register(command);

        // Map sang DTO để trả về đầy đủ MemberCode và ReferralCode cho Frontend
        RegistrationResponse responseData = RegistrationResponse.builder()
                .user(result.getUser())
                .memberCode(result.getMember().getMemberCode())
                .referralCode(result.getMember().getReferralCode())
                .build();

        // Bảo mật: Dấu password hash
        responseData.getUser().setPasswordHash(null);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(responseData, "Đăng ký hội viên thành công!"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, String>>> login(@Valid @RequestBody LoginRequest request) {
        LoginCommand command = LoginCommand.builder()
                .email(request.getEmail())
                .rawPassword(request.getPassword())
                .build();

        String token = authUseCase.login(command);

        return ResponseEntity.ok(ApiResponse.success(Map.of("accessToken", token), "Đăng nhập thành công!"));
    }
    @PostMapping("/2fa/setup")
    public ResponseEntity<ApiResponse<Map<String, String>>> setup2FA(
            @RequestParam UUID userId) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        authUseCase.setup2FA(userId),
                        "Vui lòng quét mã QR"
                )
        );
    }

    @PostMapping("/2fa/verify")
    public ResponseEntity<ApiResponse<String>> verify2FA(
            @RequestParam UUID userId,
            @RequestParam int code) {

        authUseCase.enable2FA(userId, code);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Đã kích hoạt bảo mật 2 lớp thành công!"
                )
        );
    }
    @PostMapping("/social-login")
    public ResponseEntity<ApiResponse<Map<String, String>>> socialLogin(@RequestBody SocialLoginCommand command) {
        String token = authUseCase.socialLogin(command);
        return ResponseEntity.ok(ApiResponse.success(Map.of("accessToken", token), "Đăng nhập Social thành công!"));
    }
}