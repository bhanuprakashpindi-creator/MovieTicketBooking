package com.bhanu.movie_booking_system.service;

import com.bhanu.movie_booking_system.dto.request.UserRequest;
import com.bhanu.movie_booking_system.dto.response.UserResponse;
import com.bhanu.movie_booking_system.entity.User;


public interface UserService {
    UserResponse registerUser(UserRequest userRequest);

    String verify(User user);
}
