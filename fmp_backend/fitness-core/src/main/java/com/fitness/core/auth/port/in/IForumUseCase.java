package com.fitness.core.auth.port.in;

import com.fitness.core.auth.domain.ForumThread;
import java.util.UUID;

public interface IForumUseCase {
    ForumThread createThread(UUID authorId, ForumThread thread);
}