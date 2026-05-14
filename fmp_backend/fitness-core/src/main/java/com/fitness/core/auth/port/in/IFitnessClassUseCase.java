package com.fitness.core.auth.port.in;

import com.fitness.core.auth.domain.FitnessClass;
import java.util.List;
import java.util.UUID;

// UseCase để Controller gọi vào xử lý nghiệp vụ lớp học
public interface IFitnessClassUseCase {

    // Tạo mới lớp học
    FitnessClass createClass(FitnessClass fitnessClass);

    // Cập nhật thông tin lớp học theo ID
    FitnessClass updateClass(UUID id, FitnessClass fitnessClass);

    // Lấy chi tiết một lớp học theo ID
    FitnessClass getClassById(UUID id);

    // Lấy danh sách tất cả lớp học
    List<FitnessClass> getAllClasses();

    // Xóa lớp học (thường là soft delete)
    void deleteClass(UUID id);
}