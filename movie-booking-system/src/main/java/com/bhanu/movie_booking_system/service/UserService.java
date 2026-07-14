package com.bhanu.movie_booking_system.service;

import com.bhanu.movie_booking_system.dto.request.UserRequest;
import com.bhanu.movie_booking_system.dto.response.UserResponse;


public interface UserService {
    UserResponse registerUser(UserRequest userRequest);
}
