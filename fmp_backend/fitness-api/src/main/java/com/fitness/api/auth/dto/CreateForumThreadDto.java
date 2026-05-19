package com.fitness.api.auth.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class CreateForumThreadDto {
    private UUID categoryId;
    private String title;
    private String content;
    // Tuyệt đối không có viewCount hay authorId ở đây để tránh giả mạo
}