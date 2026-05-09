package com.fitness.core.auth.port.out;

import com.fitness.core.auth.domain.Address;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IAddressRepositoryPort {
    Address save(Address address);
    List<Address> findByUserId(UUID userId);
    Optional<Address> findById(UUID addressId);
    void delete(UUID addressId);
}