package com.fitness.core.auth.port.out;

import com.fitness.core.auth.domain.Branch;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IBranchRepositoryPort {
    Branch save(Branch branch);
    List<Branch> findAllActive();
    Optional<Branch> findById(UUID id);
    Optional<Branch> findByCode(String code);
    void softDelete(UUID id);
}