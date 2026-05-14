package com.fitness.core.auth.service;

import com.fitness.core.auth.domain.Room;
import com.fitness.core.auth.port.in.IRoomUseCase;
import com.fitness.core.auth.port.out.IRoomRepositoryPort;
import com.fitness.core.common.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor // Tự inject roomRepositoryPort
public class RoomService implements IRoomUseCase {

    // Port kết nối xuống tầng Infrastructure
    private final IRoomRepositoryPort roomRepositoryPort;

    @Override
    @Transactional
    public Room createRoom(Room room) {
        // Tạo phòng mới (có thể thêm check branchId tồn tại)
        return roomRepositoryPort.save(room);
    }

    @Override
    @Transactional
    public Room updateRoom(UUID id, Room room) {
        // Tìm phòng theo id, không có thì báo lỗi
        Room existing = roomRepositoryPort.findById(id)
                .orElseThrow(() -> new DomainException(
                        "ROOM_NOT_FOUND",
                        "Phòng tập không tồn tại"
                ));

        // Cập nhật thông tin phòng
        existing.setName(room.getName());
        existing.setCapacity(room.getCapacity());
        existing.setFacilities(room.getFacilities());

        // Lưu lại vào DB
        return roomRepositoryPort.save(existing);
    }

    @Override
    public List<Room> getRoomsByBranch(UUID branchId) {
        // Lấy danh sách phòng theo chi nhánh
        return roomRepositoryPort.findByBranchId(branchId);
    }

    @Override
    @Transactional
    public void deleteRoom(UUID id) {
        // Xóa phòng
        roomRepositoryPort.delete(id);
    }
}