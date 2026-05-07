package com.fitness.api.auth.controller;

import com.fitness.api.auth.dto.LoginRequest;
import com.fitness.api.auth.dto.RegisterRequest;
import com.fitness.api.auth.dto.RegistrationResponse;
import com.fitness.common.response.ApiResponse;
import com.fitness.core.auth.domain.AuthResult;
import com.fitness.core.auth.port.in.IAuthUseCase;
import com.fitness.core.auth.port.in.LoginCommand;
import com.fitness.core.auth.port.in.RegisterCommand;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/auth")
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
}