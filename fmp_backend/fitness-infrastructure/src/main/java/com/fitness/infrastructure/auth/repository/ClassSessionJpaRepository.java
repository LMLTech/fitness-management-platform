package com.fitness.infrastructure.auth.repository;

import com.fitness.infrastructure.auth.entity.ClassSessionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Repository
public interface ClassSessionJpaRepository extends JpaRepository<ClassSessionJpaEntity, UUID> {

    // Đổi về COUNT(*) và kiểu trả về là int
    @Query(value = "SELECT COUNT(*) FROM class_sessions s WHERE s.trainer_id = :trainerId " +
            "AND s.date = :date AND s.status != 'Cancelled' AND s.deleted_at IS NULL " +
            "AND ((s.start_time <= :startTime AND ADDTIME(s.start_time, '01:00:00') > :startTime) " +
            "OR (:startTime <= s.start_time AND ADDTIME(:startTime, '01:00:00') > s.start_time))", nativeQuery = true)
    int countTrainerConflicts(@Param("trainerId") UUID trainerId,
                              @Param("date") LocalDate date,
                              @Param("startTime") LocalTime startTime);

    // Đổi về COUNT(*) và kiểu trả về là int
    @Query(value = "SELECT COUNT(*) FROM class_sessions s WHERE s.room_id = :roomId " +
            "AND s.date = :date AND s.status != 'Cancelled' AND s.deleted_at IS NULL " +
            "AND ((s.start_time <= :startTime AND ADDTIME(s.start_time, '01:00:00') > :startTime) " +
            "OR (:startTime <= s.start_time AND ADDTIME(:startTime, '01:00:00') > s.start_time))", nativeQuery = true)
    int countRoomConflicts(@Param("roomId") UUID roomId,
                           @Param("date") LocalDate date,
                           @Param("startTime") LocalTime startTime);
}