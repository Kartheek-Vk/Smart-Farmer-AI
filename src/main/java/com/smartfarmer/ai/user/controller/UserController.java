package com.smartfarmer.ai.user.controller;

import com.smartfarmer.ai.common.api.ApiResponse;
import com.smartfarmer.ai.common.api.ApiResponseFactory;
import com.smartfarmer.ai.security.CurrentUserService;
import com.smartfarmer.ai.user.dto.ChangePasswordRequest;
import com.smartfarmer.ai.user.dto.UpdateUserPreferenceRequest;
import com.smartfarmer.ai.user.dto.UpdateUserRequest;
import com.smartfarmer.ai.user.dto.UserPreferenceResponse;
import com.smartfarmer.ai.user.dto.UserResponse;
import com.smartfarmer.ai.user.entity.User;
import com.smartfarmer.ai.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Users", description = "Profile, password and preferences of the authenticated user")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final CurrentUserService currentUserService;
    private final ApiResponseFactory responseFactory;

    public UserController(UserService userService, CurrentUserService currentUserService, ApiResponseFactory responseFactory) {
        this.userService = userService;
        this.currentUserService = currentUserService;
        this.responseFactory = responseFactory;
    }

    @GetMapping("/me")
    public ApiResponse<UserResponse> getMe(HttpServletRequest request) {
        User user = currentUserService.currentUser();
        return responseFactory.success("Current user retrieved successfully", userService.getMe(user), request);
    }

    @PutMapping("/me")
    public ApiResponse<UserResponse> updateMe(@Valid @RequestBody UpdateUserRequest updateRequest, HttpServletRequest request) {
        User user = currentUserService.currentUser();
        return responseFactory.success("Profile updated successfully", userService.updateMe(user, updateRequest), request);
    }

    @PatchMapping("/me/password")
    public ApiResponse<Void> changePassword(@Valid @RequestBody ChangePasswordRequest passwordRequest, HttpServletRequest request) {
        userService.changePassword(currentUserService.currentUser(), passwordRequest);
        return responseFactory.success("Password changed successfully", null, request);
    }

    @GetMapping("/me/preferences")
    public ApiResponse<UserPreferenceResponse> getPreferences(HttpServletRequest request) {
        User user = currentUserService.currentUser();
        return responseFactory.success("Preferences retrieved successfully", userService.getPreferences(user), request);
    }

    @PutMapping("/me/preferences")
    public ApiResponse<UserPreferenceResponse> updatePreferences(@Valid @RequestBody UpdateUserPreferenceRequest body,
                                                                 HttpServletRequest request) {
        User user = currentUserService.currentUser();
        return responseFactory.success("Preferences updated successfully", userService.updatePreferences(user, body), request);
    }

    @DeleteMapping("/me")
    public ApiResponse<Void> deleteMe(HttpServletRequest request) {
        userService.deleteMe(currentUserService.currentUser());
        return responseFactory.success("Account deactivated successfully", null, request);
    }
}
