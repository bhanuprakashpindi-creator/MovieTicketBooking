package com.bhanu.movie_booking_system.controller;

import com.bhanu.movie_booking_system.dto.request.UserRequest;
import com.bhanu.movie_booking_system.dto.response.UserResponse;
import com.bhanu.movie_booking_system.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController{
    @Autowired
    UserService userService;
    @PostMapping("/register")
    public UserResponse registerUser(@Valid  @RequestBody UserRequest userRequest)
    {
        return userService.registerUser(userRequest);
    }


}
