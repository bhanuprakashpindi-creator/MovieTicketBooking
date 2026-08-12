package com.bhanu.movie_booking_system.service;

import com.bhanu.movie_booking_system.dto.request.LoginRequest;
import com.bhanu.movie_booking_system.dto.request.UserRequest;
import com.bhanu.movie_booking_system.dto.response.LoginResponse;
import com.bhanu.movie_booking_system.dto.response.UserResponse;

public interface AuthService {

    UserResponse register(UserRequest userRequest);

    LoginResponse login(LoginRequest loginRequest);
}