package com.fitness.infrastructure.auth.repository;

import com.fitness.infrastructure.auth.entity.BookingJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface BookingJpaRepository extends JpaRepository<BookingJpaEntity, UUID> {

    // Đếm số lượng học viên đã đặt chỗ thành công và chưa hủy của buổi học này
    long countBySessionIdAndStatusAndDeletedAtIsNull(UUID sessionId, String status);

    // Kiểm tra xem hội viên này đã đặt chỗ buổi học này trước đó chưa
    boolean existsByMemberIdAndSessionIdAndStatusAndDeletedAtIsNull(UUID memberId, UUID sessionId, String status);

    Optional<BookingJpaEntity> findByMemberIdAndSessionIdAndDeletedAtIsNull(UUID memberId, UUID sessionId);
}