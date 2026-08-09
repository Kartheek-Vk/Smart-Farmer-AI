package com.smartfarmer.ai.user.controller;

import com.smartfarmer.ai.common.api.ApiResponse;
import com.smartfarmer.ai.common.api.ApiResponseFactory;
import com.smartfarmer.ai.security.CurrentUserService;
import com.smartfarmer.ai.user.dto.ChangePasswordRequest;
import com.smartfarmer.ai.user.dto.UpdateUserRequest;
import com.smartfarmer.ai.user.dto.UserResponse;
import com.smartfarmer.ai.user.entity.User;
import com.smartfarmer.ai.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

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
        UserResponse response = userService.getMe(user);
        return responseFactory.success("Current user retrieved successfully", response, request);
    }

    @PutMapping("/me")
    public ApiResponse<UserResponse> updateMe(@Valid @RequestBody UpdateUserRequest updateRequest, HttpServletRequest request) {
        User user = currentUserService.currentUser();
        UserResponse response = userService.updateMe(user, updateRequest);
        return responseFactory.success("Profile updated successfully", response, request);
    }

    @PatchMapping("/me/password")
    public ApiResponse<Void> changePassword(@Valid @RequestBody ChangePasswordRequest passwordRequest, HttpServletRequest request) {
        User user = currentUserService.currentUser();
        userService.changePassword(user, passwordRequest);
        return responseFactory.success("Password changed successfully", null, request);
    }

    @DeleteMapping("/me")
    public ApiResponse<Void> deleteMe(HttpServletRequest request) {
        User user = currentUserService.currentUser();
        userService.deleteMe(user);
        return responseFactory.success("Account deleted successfully", null, request);
    }
}
