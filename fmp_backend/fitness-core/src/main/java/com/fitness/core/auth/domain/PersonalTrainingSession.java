package com.fitness.core.auth.domain;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonalTrainingSession {
    private UUID sessionId; // Khóa chính đồng thời là FK trỏ sang class_sessions
    private UUID memberId;
    private String objectives; // Mục tiêu của buổi tập (Ví dụ: "Tập trung nhóm cơ ngực")
}