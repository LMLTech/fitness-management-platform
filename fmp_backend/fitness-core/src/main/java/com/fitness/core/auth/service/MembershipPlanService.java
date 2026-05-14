package com.fitness.core.auth.service;

import com.fitness.core.auth.domain.MembershipPlan;
import com.fitness.core.auth.port.in.IMembershipPlanUseCase;
import com.fitness.core.auth.port.out.IMembershipPlanRepositoryPort;
import com.fitness.core.common.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MembershipPlanService implements IMembershipPlanUseCase {

    private final IMembershipPlanRepositoryPort repositoryPort;

    @Override
    @Transactional
    public MembershipPlan createPlan(MembershipPlan plan) {
        if (repositoryPort.existsByName(plan.getName())) {
            throw new DomainException("PLAN_ALREADY_EXISTS", "Tên gói tập này đã được cấu hình từ trước");
        }
        if (plan.getBasePrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainException("INVALID_PRICE", "Giá gói tập không thể nhỏ hơn 0đ");
        }
        if (plan.getDurationMonths() <= 0) {
            throw new DomainException("INVALID_DURATION", "Thời hạn hiệu lực của gói tập phải từ 1 tháng trở lên");
        }
        return repositoryPort.save(plan);
    }

    @Override
    @Transactional
    public MembershipPlan updatePlan(UUID id, MembershipPlan plan) {
        MembershipPlan existing = repositoryPort.findById(id)
                .orElseThrow(() -> new DomainException("PLAN_NOT_FOUND", "Gói tập không tồn tại hệ thống"));

        if (!existing.getName().equalsIgnoreCase(plan.getName()) && repositoryPort.existsByName(plan.getName())) {
            throw new DomainException("PLAN_ALREADY_EXISTS", "Tên gói tập cập nhật đã bị trùng lặp");
        }

        existing.setName(plan.getName());
        existing.setBasePrice(plan.getBasePrice());
        existing.setDurationMonths(plan.getDurationMonths());
        existing.setPlanType(plan.getPlanType());
        existing.setMaxSessionsPerMonth(plan.getMaxSessionsPerMonth());

        // Cập nhật lại bộ quyền lợi (Xóa cũ nạp mới nhờ orphanRemoval = true)
        existing.getPermissions().clear();
        existing.getPermissions().addAll(plan.getPermissions());

        return repositoryPort.save(existing);
    }

    @Override
    public MembershipPlan getPlanById(UUID id) {
        return repositoryPort.findById(id)
                .orElseThrow(() -> new DomainException("PLAN_NOT_FOUND", "Không tìm thấy thông tin gói tập"));
    }

    @Override
    public List<MembershipPlan> getAllPlans() {
        return repositoryPort.findAll();
    }

    @Override
    @Transactional
    public void deletePlan(UUID id) {
        if (!repositoryPort.findById(id).isPresent()) {
            throw new DomainException("PLAN_NOT_FOUND", "Gói tập không tồn tại hoặc đã xóa mềm trước đó");
        }
        repositoryPort.delete(id);
    }
}