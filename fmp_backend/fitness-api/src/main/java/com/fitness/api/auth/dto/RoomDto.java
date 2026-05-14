package com.fitness.api.auth.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class RoomDto {

    // ID của chi nhánh mà phòng này thuộc về
    private UUID branchId;

    // Tên phòng tập
    private String name;

    // Sức chứa tối đa của phòng
    private Integer capacity;

    // Danh sách tiện ích trong phòng (VD: máy lạnh, gương, loa...)
    private String facilities;
}