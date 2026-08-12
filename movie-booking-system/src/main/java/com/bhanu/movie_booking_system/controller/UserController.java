package com.bhanu.movie_booking_system.controller;

import com.bhanu.movie_booking_system.dto.request.ChangePasswordRequest;
import com.bhanu.movie_booking_system.dto.request.UpdateUserRequest;
import com.bhanu.movie_booking_system.dto.response.UserResponse;
import com.bhanu.movie_booking_system.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserResponse getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @PutMapping("/{userId}")
    public UserResponse updateUser(@PathVariable Long userId,
                                   @Valid @RequestBody UpdateUserRequest request) {

        return userService.updateUser(userId, request);
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable Long userId) {

        userService.deleteUser(userId);

        return "User Deleted Successfully";
    }

    @GetMapping("/profile")
    public UserResponse getProfile(Authentication authentication) {

        return userService.getProfile(authentication.getName());
    }

    @PutMapping("/profile")
    public UserResponse updateProfile(Authentication authentication,
                                      @Valid @RequestBody UpdateUserRequest request) {

        return userService.updateProfile(authentication.getName(), request);
    }

    @PutMapping("/change-password")
    public String changePassword(Authentication authentication,
                                 @Valid @RequestBody ChangePasswordRequest request) {

        userService.changePassword(authentication.getName(), request);

        return "Password Changed Successfully";
    }
}