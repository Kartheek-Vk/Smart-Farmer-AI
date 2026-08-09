package com.smartfarmer.ai.user.service;

import com.smartfarmer.ai.common.enums.UserStatus;
import com.smartfarmer.ai.exception.BusinessException;
import com.smartfarmer.ai.user.dto.ChangePasswordRequest;
import com.smartfarmer.ai.user.dto.UpdateUserPreferenceRequest;
import com.smartfarmer.ai.user.dto.UpdateUserRequest;
import com.smartfarmer.ai.user.dto.UserPreferenceResponse;
import com.smartfarmer.ai.user.dto.UserResponse;
import com.smartfarmer.ai.user.entity.Role;
import com.smartfarmer.ai.user.entity.User;
import com.smartfarmer.ai.user.entity.UserPreference;
import com.smartfarmer.ai.user.repository.UserPreferenceRepository;
import com.smartfarmer.ai.user.repository.UserRepository;
import java.util.stream.Collectors;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserPreferenceRepository userPreferenceRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       UserPreferenceRepository userPreferenceRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userPreferenceRepository = userPreferenceRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public UserResponse getMe(User currentUser) {
        return mapToResponse(currentUser);
    }

    @Transactional
    public UserResponse updateMe(User currentUser, UpdateUserRequest request) {
        if (request.firstName() != null) {
            currentUser.setFirstName(request.firstName());
        }
        if (request.lastName() != null) {
            currentUser.setLastName(request.lastName());
        }
        return mapToResponse(userRepository.save(currentUser));
    }

    @Transactional
    public void changePassword(User currentUser, ChangePasswordRequest request) {
        if (!passwordEncoder.matches(request.currentPassword(), currentUser.getPasswordHash())) {
            throw new BusinessException("Current password is incorrect");
        }
        if (passwordEncoder.matches(request.newPassword(), currentUser.getPasswordHash())) {
            throw new BusinessException("New password must be different from the current password");
        }
        currentUser.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        userRepository.save(currentUser);
    }

    @Transactional
    public void deleteMe(User currentUser) {
        currentUser.setStatus(UserStatus.INACTIVE);
        userRepository.save(currentUser);
    }

    @Transactional
    public UserPreferenceResponse getPreferences(User currentUser) {
        return mapToPreferenceResponse(loadOrCreatePreferences(currentUser));
    }

    @Transactional
    public UserPreferenceResponse updatePreferences(User currentUser, UpdateUserPreferenceRequest request) {
        UserPreference preference = loadOrCreatePreferences(currentUser);
        preference.setLanguage(request.language());
        preference.setMeasurementUnit(request.measurementUnit());
        preference.setEmailNotificationsEnabled(request.emailNotificationsEnabled());
        preference.setPushNotificationsEnabled(request.pushNotificationsEnabled());
        preference.setWeatherAlertsEnabled(request.weatherAlertsEnabled());
        return mapToPreferenceResponse(userPreferenceRepository.save(preference));
    }

    private UserPreference loadOrCreatePreferences(User currentUser) {
        return userPreferenceRepository.findByUserId(currentUser.getId())
                .orElseGet(() -> {
                    UserPreference preference = new UserPreference();
                    preference.setUser(currentUser);
                    return userPreferenceRepository.save(preference);
                });
    }

    private UserPreferenceResponse mapToPreferenceResponse(UserPreference preference) {
        return new UserPreferenceResponse(
                preference.getLanguage(),
                preference.getMeasurementUnit(),
                preference.isEmailNotificationsEnabled(),
                preference.isPushNotificationsEnabled(),
                preference.isWeatherAlertsEnabled());
    }

    private UserResponse mapToResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getStatus() != null ? user.getStatus().name() : null,
                user.getRoles().stream().map(Role::getName).map(Enum::name).collect(Collectors.toSet()),
                user.getCreatedAt());
    }
}
