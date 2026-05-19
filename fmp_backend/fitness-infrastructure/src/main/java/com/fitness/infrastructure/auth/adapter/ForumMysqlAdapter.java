package com.fitness.infrastructure.auth.adapter;

import com.fitness.core.auth.domain.ForumThread;
import com.fitness.core.auth.port.out.IForumRepositoryPort;
import com.fitness.infrastructure.auth.entity.ForumThreadEntity;
import com.fitness.infrastructure.auth.repository.ForumCategoryJpaRepository;
import com.fitness.infrastructure.auth.repository.ForumThreadJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ForumMysqlAdapter implements IForumRepositoryPort {

    private final ForumThreadJpaRepository threadRepo;
    private final ForumCategoryJpaRepository categoryRepo;

    @Override
    public ForumThread saveThread(ForumThread thread) {
        ForumThreadEntity entity = ForumThreadEntity.builder()
                .id(thread.getId())
                .categoryId(thread.getCategoryId())
                .authorId(thread.getAuthorId())
                .title(thread.getTitle())
                .content(thread.getContent())
                .viewCount(thread.getViewCount())
                .createdAt(thread.getCreatedAt())
                .build();
        threadRepo.save(entity);
        return thread;
    }

    @Override
    public boolean isCategoryExists(UUID categoryId) {
        return categoryRepo.existsById(categoryId);
    }
}