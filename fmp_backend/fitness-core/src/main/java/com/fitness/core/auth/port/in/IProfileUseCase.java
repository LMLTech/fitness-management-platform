package com.fitness.core.auth.port.in;

import com.fitness.core.auth.domain.Address;
import com.fitness.core.auth.domain.User;
import java.util.List;
import java.util.UUID;

public interface IProfileUseCase {
    User getProfile(UUID userId);
    User updateProfile(UUID userId, String fullName, String gender, String avatarUrl);
    void changePassword(UUID userId, String oldPassword, String newPassword);

    // Address management
    Address addAddress(Address address);
    List<Address> getUserAddresses(UUID userId);
    void deleteAddress(UUID addressId, UUID userId);
}