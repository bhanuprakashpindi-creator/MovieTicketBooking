package com.bhanu.movie_booking_system.service.Impl;

import com.bhanu.movie_booking_system.dto.request.ChangePasswordRequest;
import com.bhanu.movie_booking_system.dto.request.UpdateUserRequest;
import com.bhanu.movie_booking_system.dto.response.UserResponse;
import com.bhanu.movie_booking_system.entity.User;
import com.bhanu.movie_booking_system.exception.UserNotFoundException;
import com.bhanu.movie_booking_system.repository.UserRepository;
import com.bhanu.movie_booking_system.service.UserService;
import com.bhanu.movie_booking_system.transformer.UserTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<UserResponse> getAllUsers() {

        List<User> users = userRepository.findAll();

        List<UserResponse> responses = new ArrayList<>();

        for (User user : users) {
            responses.add(UserTransformer.userToUserResponse(user));
        }

        return responses;
    }

    @Override
    public UserResponse getUserById(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User Not Found"));

        return UserTransformer.userToUserResponse(user);
    }

    @Override
    public UserResponse updateUser(Long userId,
                                   UpdateUserRequest updateUserRequest) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User Not Found"));

        user.setUserName(updateUserRequest.getUserName());
        user.setEmail(updateUserRequest.getEmail());
        user.setPhoneNumber(updateUserRequest.getPhoneNumber());

        User updatedUser = userRepository.save(user);

        return UserTransformer.userToUserResponse(updatedUser);
    }

    @Override
    public void deleteUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User Not Found"));

        userRepository.delete(user);
    }

    @Override
    public UserResponse getProfile(String username) {

        User user = userRepository.findByUserName(username)
                .orElseThrow(() ->
                        new UserNotFoundException("User Not Found"));

        return UserTransformer.userToUserResponse(user);
    }

    @Override
    public UserResponse updateProfile(String username,
                                      UpdateUserRequest updateUserRequest) {

        User user = userRepository.findByUserName(username)
                .orElseThrow(() ->
                        new UserNotFoundException("User Not Found"));

        user.setUserName(updateUserRequest.getUserName());
        user.setEmail(updateUserRequest.getEmail());
        user.setPhoneNumber(updateUserRequest.getPhoneNumber());

        User updatedUser = userRepository.save(user);

        return UserTransformer.userToUserResponse(updatedUser);
    }

    @Override
    public void changePassword(String username,
                               ChangePasswordRequest request) {

        User user = userRepository.findByUserName(username)
                .orElseThrow(() ->
                        new UserNotFoundException("User Not Found"));

        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPassword())) {

            throw new RuntimeException("Current Password is Incorrect");
        }

        user.setPassword(
                passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);
    }
}