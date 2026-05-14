package com.fitness.core.auth.service;

import com.fitness.core.auth.domain.FitnessClass;
import com.fitness.core.auth.port.in.IFitnessClassUseCase;
import com.fitness.core.auth.port.out.IFitnessClassRepositoryPort;
import com.fitness.core.common.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service // Đánh dấu đây là Service xử lý nghiệp vụ
@RequiredArgsConstructor // Tự inject constructor cho final field
public class FitnessClassService implements IFitnessClassUseCase {

    // Gọi xuống Repository Port để thao tác dữ liệu
    private final IFitnessClassRepositoryPort repositoryPort;

    @Override
    @Transactional // Đảm bảo giao dịch DB an toàn
    public FitnessClass createClass(FitnessClass fitnessClass) {

        // Kiểm tra tên lớp có bị trùng không
        if (repositoryPort.existsByName(fitnessClass.getName())) {
            throw new DomainException("CLASS_ALREADY_EXISTS", "Tên môn học này đã tồn tại trong hệ thống");
        }

        // Kiểm tra sức chứa phải lớn hơn 0
        if (fitnessClass.getDefaultMaxCapacity() <= 0) {
            throw new DomainException("INVALID_CAPACITY", "Sức chứa tối đa mặc định phải lớn hơn 0");
        }

        // Lưu lớp học mới
        return repositoryPort.save(fitnessClass);
    }

    @Override
    @Transactional
    public FitnessClass updateClass(UUID id, FitnessClass fitnessClass) {

        // Tìm lớp học cần cập nhật
        FitnessClass existing = repositoryPort.findById(id)
                .orElseThrow(() -> new DomainException("CLASS_NOT_FOUND", "Không tìm thấy môn học yêu cầu"));

        // Nếu đổi tên thì kiểm tra tên mới có bị trùng không
        if (!existing.getName().equalsIgnoreCase(fitnessClass.getName())
                && repositoryPort.existsByName(fitnessClass.getName())) {
            throw new DomainException("CLASS_ALREADY_EXISTS", "Tên môn học mới đã tồn tại");
        }

        // Cập nhật thông tin mới
        existing.setName(fitnessClass.getName());
        existing.setDescription(fitnessClass.getDescription());
        existing.setClassType(fitnessClass.getClassType());
        existing.setDifficulty(fitnessClass.getDifficulty());
        existing.setDefaultMaxCapacity(fitnessClass.getDefaultMaxCapacity());

        // Lưu cập nhật
        return repositoryPort.save(existing);
    }

    @Override
    public FitnessClass getClassById(UUID id) {

        // Lấy thông tin lớp học theo ID
        return repositoryPort.findById(id)
                .orElseThrow(() -> new DomainException("CLASS_NOT_FOUND", "Không tìm thấy thông tin môn học"));
    }

    @Override
    public List<FitnessClass> getAllClasses() {

        // Lấy danh sách tất cả lớp học
        return repositoryPort.findAll();
    }

    @Override
    @Transactional
    public void deleteClass(UUID id) {

        // Kiểm tra lớp học có tồn tại không
        if (!repositoryPort.findById(id).isPresent()) {
            throw new DomainException("CLASS_NOT_FOUND", "Môn học không tồn tại hoặc đã bị xóa trước đó");
        }

        // Xóa lớp học
        repositoryPort.delete(id);
    }
}