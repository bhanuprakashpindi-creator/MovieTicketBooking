package com.bhanu.movie_booking_system.service;

import com.bhanu.movie_booking_system.dto.request.ChangePasswordRequest;
import com.bhanu.movie_booking_system.dto.request.UpdateUserRequest;
import com.bhanu.movie_booking_system.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long userId);

    UserResponse updateUser(Long userId, UpdateUserRequest updateUserRequest);

    void deleteUser(Long userId);

    UserResponse getProfile(String username);

    UserResponse updateProfile(String username, UpdateUserRequest updateUserRequest);

    void changePassword(String username, ChangePasswordRequest request);
}