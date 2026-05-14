package com.fitness.core.auth.port.out;

import com.fitness.core.auth.domain.FitnessClass;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// Port để Core gọi xuống tầng Infrastructure thao tác với dữ liệu lớp học
public interface IFitnessClassRepositoryPort {

    // Lưu mới hoặc cập nhật lớp học
    FitnessClass save(FitnessClass fitnessClass);

    // Tìm lớp học theo ID
    Optional<FitnessClass> findById(UUID id);

    // Tìm lớp học theo tên
    Optional<FitnessClass> findByName(String name);

    // Lấy tất cả lớp học
    List<FitnessClass> findAll();

    // Kiểm tra tên lớp đã tồn tại chưa
    boolean existsByName(String name);

    // Xóa lớp học
    void delete(UUID id);
}