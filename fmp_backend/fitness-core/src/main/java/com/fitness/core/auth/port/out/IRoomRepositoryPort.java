package com.fitness.core.auth.port.out;

import com.fitness.core.auth.domain.Room;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// Định nghĩa các thao tác lưu/lấy/xóa dữ liệu phòng tập
public interface IRoomRepositoryPort {

    // Lưu hoặc cập nhật thông tin phòng tập
    Room save(Room room);

    // Tìm phòng theo id
    Optional<Room> findById(UUID id);

    // Lấy danh sách phòng theo chi nhánh
    List<Room> findByBranchId(UUID branchId);

    // Xóa phòng theo id
    void delete(UUID id);
}