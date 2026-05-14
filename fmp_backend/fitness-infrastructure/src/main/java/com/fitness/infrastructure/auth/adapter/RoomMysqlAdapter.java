package com.fitness.infrastructure.auth.adapter;

import com.fitness.core.auth.domain.Room;
import com.fitness.core.auth.port.out.IRoomRepositoryPort;
import com.fitness.infrastructure.auth.entity.RoomJpaEntity;
import com.fitness.infrastructure.auth.repository.RoomJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component // Adapter kết nối Core với MySQL
@RequiredArgsConstructor // Tự inject roomJpaRepository
public class RoomMysqlAdapter implements IRoomRepositoryPort {

    // Repository JPA thao tác với bảng rooms
    private final RoomJpaRepository roomJpaRepository;

    @Override
    public Room save(Room room) {
        // Map Domain -> JPA Entity để lưu DB
        RoomJpaEntity entity = RoomJpaEntity.builder()
                .id(room.getId())
                .branchId(room.getBranchId())
                .name(room.getName())
                .capacity(room.getCapacity())
                .facilities(room.getFacilities())
                .deletedAt(room.getDeletedAt())
                .build();

        // Lưu vào database
        RoomJpaEntity saved = roomJpaRepository.save(entity);

        // Map lại Entity -> Domain rồi trả về
        return mapToDomain(saved);
    }

    @Override
    public Optional<Room> findById(UUID id) {
        // Tìm phòng theo ID
        return roomJpaRepository.findById(id).map(this::mapToDomain);
    }

    @Override
    public List<Room> findByBranchId(UUID branchId) {
        // Lấy danh sách phòng theo chi nhánh
        return roomJpaRepository.findByBranchId(branchId).stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        // Soft delete: không xóa thật, chỉ gán deletedAt
        roomJpaRepository.findById(id).ifPresent(entity -> {
            entity.setDeletedAt(LocalDateTime.now());
            roomJpaRepository.save(entity);
        });
    }

    // Mapper: chuyển từ JPA Entity -> Domain
    private Room mapToDomain(RoomJpaEntity entity) {
        return Room.builder()
                .id(entity.getId())
                .branchId(entity.getBranchId())
                .name(entity.getName())
                .capacity(entity.getCapacity())
                .facilities(entity.getFacilities())
                .deletedAt(entity.getDeletedAt())
                .build();
    }
}