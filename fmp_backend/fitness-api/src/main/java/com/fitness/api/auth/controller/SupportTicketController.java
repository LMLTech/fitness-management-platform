package com.fitness.api.auth.controller;

import com.fitness.api.auth.dto.CreateTicketDto;
import com.fitness.api.auth.dto.ReplyTicketDto;
import com.fitness.common.response.ApiResponse;
import com.fitness.core.auth.domain.SupportTicket;
import com.fitness.core.auth.domain.TicketMessage;
import com.fitness.core.auth.port.in.ISupportTicketUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/support-tickets")
@RequiredArgsConstructor
public class SupportTicketController {

    private final ISupportTicketUseCase ticketUseCase;

    @PostMapping
    public ResponseEntity<ApiResponse<SupportTicket>> createTicket(@RequestBody CreateTicketDto req) {
        // Trả lại cách lấy ID từ Request Body để không bị lỗi ép kiểu JWT
        SupportTicket ticket = ticketUseCase.createTicket(
                req.getUserId(), req.getSubject(), req.getPriority(), req.getInitialMessage()
        );
        return ResponseEntity.ok(ApiResponse.success(ticket, "Tạo phiên hỗ trợ khiếu nại thành công!"));
    }

    @PostMapping("/{ticketId}/messages")
    public ResponseEntity<ApiResponse<TicketMessage>> replyTicket(
            @PathVariable UUID ticketId,
            @RequestBody ReplyTicketDto req) {

        TicketMessage message = ticketUseCase.replyToTicket(ticketId, req.getSenderId(), req.getMessage());
        return ResponseEntity.ok(ApiResponse.success(message, "Gửi tin nhắn phản hồi thành công!"));
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<ApiResponse<SupportTicket>> getTicketDetails(@PathVariable UUID ticketId) {
        SupportTicket ticket = ticketUseCase.getTicketDetails(ticketId);
        return ResponseEntity.ok(ApiResponse.success(ticket, "Lấy chi tiết lịch sử chat hỗ trợ thành công!"));
    }

    @PutMapping("/{ticketId}/resolve")
    public ResponseEntity<ApiResponse<Void>> resolveTicket(@PathVariable UUID ticketId) {
        ticketUseCase.resolveTicket(ticketId);
        return ResponseEntity.ok(ApiResponse.success(null, "Xử lý và đóng ticket thành công!"));
    }
}