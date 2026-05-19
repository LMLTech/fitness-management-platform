package com.fitness.core.auth.service;

import com.fitness.core.auth.domain.ForumThread;
import com.fitness.core.auth.port.in.IForumUseCase;
import com.fitness.core.auth.port.out.IForumRepositoryPort;
import com.fitness.core.common.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ForumService implements IForumUseCase {

    private final IForumRepositoryPort forumRepoPort;

    @Override
    @Transactional
    public ForumThread createThread(UUID authorId, ForumThread thread) {
        if (!forumRepoPort.isCategoryExists(thread.getCategoryId())) {
            throw new DomainException("INVALID_CATEGORY", "Chuyên mục diễn đàn không tồn tại");
        }

        if (thread.getTitle() == null || thread.getTitle().trim().isEmpty()) {
            throw new DomainException("EMPTY_TITLE", "Tiêu đề bài viết không được để trống");
        }

        thread.setId(UUID.randomUUID());
        thread.setAuthorId(authorId);
        thread.setViewCount(0);
        thread.setCreatedAt(LocalDateTime.now());

        return forumRepoPort.saveThread(thread);
    }
}