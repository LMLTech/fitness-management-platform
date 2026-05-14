package com.fitness.api.auth.controller;

import com.fitness.api.auth.dto.RoomDto;
import com.fitness.common.response.ApiResponse;
import com.fitness.core.auth.domain.Room;
import com.fitness.core.auth.port.in.IRoomUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController // Đánh dấu đây là REST API Controller
@RequestMapping("/api/v1/admin/rooms") // Base URL cho API quản lý phòng
@RequiredArgsConstructor // Tự inject roomUseCase
@PreAuthorize("hasAuthority('ROLE_ADMIN')") // Chỉ ADMIN mới được gọi API này
public class RoomController {

    // Gọi sang tầng Service xử lý nghiệp vụ
    private final IRoomUseCase roomUseCase;

    @PostMapping
    public ResponseEntity<ApiResponse<Room>> create(@RequestBody RoomDto dto) {
        // Chuyển DTO -> Domain Room
        Room room = Room.builder()
                .branchId(dto.getBranchId())
                .name(dto.getName())
                .capacity(dto.getCapacity())
                .facilities(dto.getFacilities())
                .build();

        // Tạo phòng mới
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        roomUseCase.createRoom(room),
                        "Tạo phòng tập thành công"
                ));
    }

    @GetMapping("/branch/{branchId}")
    public ResponseEntity<ApiResponse<List<Room>>> getByBranch(
            @PathVariable UUID branchId
    ) {
        // Lấy danh sách phòng theo chi nhánh
        return ResponseEntity.ok(
                ApiResponse.success(
                        roomUseCase.getRoomsByBranch(branchId),
                        "Lấy danh sách phòng thành công"
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Room>> update(
            @PathVariable UUID id,
            @RequestBody RoomDto dto
    ) {
        // Chuyển DTO -> Domain Room để cập nhật
        Room room = Room.builder()
                .name(dto.getName())
                .capacity(dto.getCapacity())
                .facilities(dto.getFacilities())
                .build();

        // Cập nhật phòng
        return ResponseEntity.ok(
                ApiResponse.success(
                        roomUseCase.updateRoom(id, room),
                        "Cập nhật thành công"
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id
    ) {
        // Xóa phòng
        roomUseCase.deleteRoom(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        null,
                        "Xóa phòng tập thành công"
                )
        );
    }
}