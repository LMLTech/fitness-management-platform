package com.fitness.core.auth.port.out;

import com.fitness.core.auth.domain.ForumThread;
import java.util.UUID;

public interface IForumRepositoryPort {
    ForumThread saveThread(ForumThread thread);
    boolean isCategoryExists(UUID categoryId);
}