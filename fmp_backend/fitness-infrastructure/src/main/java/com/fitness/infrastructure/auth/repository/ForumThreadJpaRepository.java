package com.fitness.infrastructure.auth.repository;

import com.fitness.infrastructure.auth.entity.ForumThreadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface ForumThreadJpaRepository extends JpaRepository<ForumThreadEntity, UUID> {
}