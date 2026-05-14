package com.fitness.core.auth.port.in;

import com.fitness.core.auth.domain.Room;
import java.util.List;
import java.util.UUID;

// Định nghĩa các chức năng quản lý phòng tập
public interface IRoomUseCase {

    // Tạo phòng tập mới
    Room createRoom(Room room);

    // Cập nhật thông tin phòng theo id
    Room updateRoom(UUID id, Room room);

    // Lấy danh sách phòng theo chi nhánh (branchId)
    List<Room> getRoomsByBranch(UUID branchId);

    // Xóa phòng theo id
    void deleteRoom(UUID id);
}