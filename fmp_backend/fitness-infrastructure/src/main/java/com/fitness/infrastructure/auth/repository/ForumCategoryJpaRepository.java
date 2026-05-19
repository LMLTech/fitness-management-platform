package com.fitness.infrastructure.auth.repository;

import com.fitness.infrastructure.auth.entity.ForumCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface ForumCategoryJpaRepository extends JpaRepository<ForumCategoryEntity, UUID> {
    // Kế thừa sẵn các hàm CRUD (findById, save, delete,...) từ Spring Data JPA
}