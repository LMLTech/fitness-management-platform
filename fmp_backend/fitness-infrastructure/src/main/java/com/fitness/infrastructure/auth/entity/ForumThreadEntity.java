package com.fitness.infrastructure.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "forum_threads")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForumThreadEntity {
    @Id
    private UUID id;
    @Column(name = "category_id")
    private UUID categoryId;
    @Column(name = "author_id")
    private UUID authorId;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    @Column(name = "view_count")
    private Integer viewCount;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}