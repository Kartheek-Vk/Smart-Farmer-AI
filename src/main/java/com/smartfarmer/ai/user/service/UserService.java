package com.smartfarmer.ai.user.service;

import com.smartfarmer.ai.common.enums.UserStatus;
import com.smartfarmer.ai.exception.BusinessException;
import com.smartfarmer.ai.user.dto.ChangePasswordRequest;
import com.smartfarmer.ai.user.dto.UpdateUserRequest;
import com.smartfarmer.ai.user.dto.UserResponse;
import com.smartfarmer.ai.user.entity.Role;
import com.smartfarmer.ai.user.entity.User;
import com.smartfarmer.ai.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse getMe(User currentUser) {
        return mapToResponse(currentUser);
    }

    @Transactional
    public UserResponse updateMe(User currentUser, UpdateUserRequest request) {
        currentUser.setFirstName(request.firstName());
        currentUser.setLastName(request.lastName());
        
        User updatedUser = userRepository.save(currentUser);
        return mapToResponse(updatedUser);
    }

    @Transactional
    public void changePassword(User currentUser, ChangePasswordRequest request) {
        if (!passwordEncoder.matches(request.currentPassword(), currentUser.getPasswordHash())) {
            throw new BusinessException("Invalid current password");
        }
        
        currentUser.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        userRepository.save(currentUser);
    }

    @Transactional
    public void deleteMe(User currentUser) {
        currentUser.setStatus(UserStatus.INACTIVE);
        userRepository.save(currentUser);
    }

    private UserResponse mapToResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getStatus() != null ? user.getStatus().name() : null,
                user.getRoles().stream().map(Role::getName).map(Enum::name).collect(Collectors.toSet()),
                user.getCreatedAt()
        );
    }
}
