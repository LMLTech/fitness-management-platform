package com.fitness.core.auth.service;

import com.fitness.core.auth.domain.Address;
import com.fitness.core.auth.domain.User;
import com.fitness.core.auth.port.in.IProfileUseCase;
import com.fitness.core.auth.port.out.IAddressRepositoryPort;
import com.fitness.core.auth.port.out.IPasswordEncoderPort;
import com.fitness.core.auth.port.out.IUserRepositoryPort;
import com.fitness.core.common.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService implements IProfileUseCase {
    private final IUserRepositoryPort userRepositoryPort;
    private final IAddressRepositoryPort addressRepositoryPort;
    private final IPasswordEncoderPort passwordEncoderPort;

    @Override
    public User getProfile(UUID userId) {
        return userRepositoryPort.findById(userId)
                .orElseThrow(() -> new DomainException("USER_NOT_FOUND", "Không tìm thấy người dùng"));
    }

    @Override
    public User updateProfile(UUID userId, String fullName, String gender, String avatarUrl) {
        User user = getProfile(userId);
        user.setFullName(fullName);
        user.setGender(gender);
        user.setAvatarUrl(avatarUrl);
        // Lưu cập nhật thông qua Repository Port (Bạn cần thêm hàm updateProfile vào Port này)
        userRepositoryPort.updateProfile(user);
        return user;
    }

    @Override
    public void changePassword(UUID userId, String oldPassword, String newPassword) {
        User user = getProfile(userId);
        if (!passwordEncoderPort.matches(oldPassword, user.getPasswordHash())) {
            throw new DomainException("INVALID_PASSWORD", "Mật khẩu cũ không chính xác");
        }
        user.setPasswordHash(passwordEncoderPort.encode(newPassword));
        userRepositoryPort.updatePassword(userId, user.getPasswordHash());
    }

    @Override
    public Address addAddress(Address address) {
        return addressRepositoryPort.save(address);
    }

    @Override
    public List<Address> getUserAddresses(UUID userId) {
        return addressRepositoryPort.findByUserId(userId);
    }

    @Override
    public void deleteAddress(UUID addressId, UUID userId) {
        Address address = addressRepositoryPort.findById(addressId)
                .orElseThrow(() -> new DomainException("ADDRESS_NOT_FOUND", "Địa chỉ không tồn tại"));
        if (!address.getUserId().equals(userId)) {
            throw new DomainException("FORBIDDEN", "Bạn không có quyền xóa địa chỉ này");
        }
        addressRepositoryPort.delete(addressId);
    }
}