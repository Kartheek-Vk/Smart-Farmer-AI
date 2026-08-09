package com.smartfarmer.ai.security;

import com.smartfarmer.ai.common.enums.UserRole;
import com.smartfarmer.ai.exception.ResourceNotFoundException;
import com.smartfarmer.ai.exception.UnauthorizedException;
import com.smartfarmer.ai.user.entity.User;
import com.smartfarmer.ai.user.repository.UserRepository;
import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    private final UserRepository userRepository;

    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UUID currentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new UnauthorizedException("Authentication is required");
        }
        try {
            return UUID.fromString(authentication.getPrincipal().toString());
        } catch (IllegalArgumentException ex) {
            throw new UnauthorizedException("Invalid authenticated principal");
        }
    }

    public User currentUser() {
        return userRepository.findById(currentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user was not found"));
    }

    public User getCurrentUser() {
        return currentUser();
    }

    public boolean isAdmin(User user) {
        return user.getRolesAsEnumSet().contains(UserRole.ADMIN);
    }

    public boolean isCurrentUserOrAdmin(UUID userId) {
        User current = currentUser();
        return current.getId().equals(userId) || isAdmin(current);
    }
}
