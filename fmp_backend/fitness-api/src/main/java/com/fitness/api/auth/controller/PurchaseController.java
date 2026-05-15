package com.fitness.api.auth.controller;

import com.fitness.api.auth.dto.PurchaseRequestDto;
import com.fitness.common.response.ApiResponse;
import com.fitness.core.auth.domain.Subscription;
import com.fitness.core.auth.port.in.IPurchaseUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final IPurchaseUseCase purchaseUseCase;

    @PostMapping("/member/{memberId}")
    public ResponseEntity<ApiResponse<Subscription>> subscribe(
            @PathVariable UUID memberId,
            @RequestBody PurchaseRequestDto dto) {

        Subscription sub = purchaseUseCase.purchasePlan(memberId, dto.getPlanId(), dto.getPaymentMethod());
        return ResponseEntity.ok(ApiResponse.success(sub, "Đơn hàng đã được tạo. Vui lòng tiến hành thanh toán!"));
    }
}