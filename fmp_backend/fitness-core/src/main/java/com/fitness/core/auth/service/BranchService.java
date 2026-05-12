package com.fitness.core.auth.service;

import com.fitness.core.auth.domain.Branch;
import com.fitness.core.auth.port.in.IBranchUseCase;
import com.fitness.core.auth.port.out.IBranchRepositoryPort;
import com.fitness.core.common.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BranchService implements IBranchUseCase {

    private final IBranchRepositoryPort branchRepositoryPort;

    @Override
    public Branch createBranch(Branch branch) {
        if (branchRepositoryPort.findByCode(branch.getCode()).isPresent()) {
            throw new DomainException("BRANCH_CODE_EXISTS", "Mã chi nhánh đã tồn tại!");
        }
        return branchRepositoryPort.save(branch);
    }

    @Override
    public Branch updateBranch(UUID id, Branch branch) {
        branchRepositoryPort.findById(id)
                .orElseThrow(() -> new DomainException("BRANCH_NOT_FOUND", "Không tìm thấy chi nhánh"));
        branch.setId(id);
        return branchRepositoryPort.save(branch);
    }

    @Override
    public List<Branch> getAllBranches() {
        return branchRepositoryPort.findAllActive();
    }

    @Override
    public Branch getBranchById(UUID id) {
        return branchRepositoryPort.findById(id)
                .orElseThrow(() -> new DomainException("BRANCH_NOT_FOUND", "Không tìm thấy chi nhánh"));
    }

    @Override
    public void deleteBranch(UUID id) {
        branchRepositoryPort.softDelete(id);
    }
}