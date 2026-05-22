package com.fitness.infrastructure.auth.repository;

import com.fitness.infrastructure.auth.entity.WaitingListJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WaitingListJpaRepository extends JpaRepository<WaitingListJpaEntity, UUID> {

    // Tìm vị trí lớn nhất hiện tại trong hàng chờ của buổi học để cộng dồn lên
    @Query("SELECT COALESCE(MAX(w.position), 0) FROM WaitingListJpaEntity w WHERE w.sessionId = :sessionId AND w.status = 'Waiting'")
    int findMaxPositionBySessionId(@Param("sessionId") UUID sessionId);

    // Lấy ra danh sách hàng chờ xếp theo thứ tự ưu tiên ai đến trước đứng trước
    List<WaitingListJpaEntity> findBySessionIdAndStatusOrderByPositionAsc(UUID sessionId, String status);

    // Kiểm tra xem người này đã nằm trong hàng chờ của lớp này chưa
    boolean existsByMemberIdAndSessionIdAndStatus(UUID memberId, UUID sessionId, String status);

    // Tìm record Hàng chờ của 1 user cụ thể trong 1 buổi học
    Optional<WaitingListJpaEntity> findByMemberIdAndSessionId(UUID memberId, UUID sessionId);
}