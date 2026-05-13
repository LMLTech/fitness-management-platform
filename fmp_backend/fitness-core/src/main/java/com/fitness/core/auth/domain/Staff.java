package com.fitness.core.auth.domain;

import lombok.*;
import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Staff {
    private UUID userId;
    private UUID branchId;
    private String employeeId;
    private String jobTitle;
    private User user; // Thông tin user đi kèm
}
