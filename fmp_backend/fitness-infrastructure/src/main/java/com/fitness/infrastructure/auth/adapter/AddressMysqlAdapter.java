package com.fitness.infrastructure.auth.adapter;

import com.fitness.core.auth.domain.Address;
import com.fitness.core.auth.port.out.IAddressRepositoryPort;
import com.fitness.infrastructure.auth.entity.AddressJpaEntity;
import com.fitness.infrastructure.auth.repository.AddressJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AddressMysqlAdapter implements IAddressRepositoryPort {
    private final AddressJpaRepository addressRepository;

    @Override
    public Address save(Address address) {
        AddressJpaEntity entity = AddressJpaEntity.builder()
                .id(address.getId())
                .userId(address.getUserId())
                .addressType(address.getAddressType())
                .streetAddress(address.getStreetAddress())
                .ward(address.getWard())
                .district(address.getDistrict())
                .city(address.getCity())
                .country(address.getCountry())
                .build();
        AddressJpaEntity saved = addressRepository.save(entity);
        address.setId(saved.getId());
        return address;
    }

    @Override
    public List<Address> findByUserId(UUID userId) {
        return addressRepository.findByUserId(userId).stream()
                .map(this::mapToDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Address> findById(UUID addressId) {
        return addressRepository.findById(addressId).map(this::mapToDomain);
    }

    @Override
    public void delete(UUID addressId) {
        addressRepository.deleteById(addressId);
    }

    private Address mapToDomain(AddressJpaEntity entity) {
        return Address.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .addressType(entity.getAddressType())
                .streetAddress(entity.getStreetAddress())
                .ward(entity.getWard())
                .district(entity.getDistrict())
                .city(entity.getCity())
                .country(entity.getCountry())
                .build();
    }
}