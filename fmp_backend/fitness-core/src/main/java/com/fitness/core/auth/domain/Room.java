package com.fitness.core.auth.domain;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {
    private UUID id;
    private UUID branchId; // Gắn với chi nhánh nào
    private String name;    // Tên phòng
    private Integer capacity; // Sức chứa tối đa
    private String facilities; // Trang thiết bị (VD: Thảm, Gương, Loa)
    private LocalDateTime deletedAt;
}