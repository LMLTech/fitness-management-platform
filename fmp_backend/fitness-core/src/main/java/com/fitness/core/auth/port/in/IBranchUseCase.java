package com.fitness.core.auth.port.in;

import com.fitness.core.auth.domain.Branch;
import java.util.List;
import java.util.UUID;

public interface IBranchUseCase {
    Branch createBranch(Branch branch);
    Branch updateBranch(UUID id, Branch branch);
    List<Branch> getAllBranches();
    Branch getBranchById(UUID id);
    void deleteBranch(UUID id); // Soft delete
}