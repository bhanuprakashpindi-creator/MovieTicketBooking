package com.bhanu.movie_booking_system.service.Impl;

import com.bhanu.movie_booking_system.dto.request.ChangePasswordRequest;
import com.bhanu.movie_booking_system.dto.request.UpdateUserRequest;
import com.bhanu.movie_booking_system.dto.response.UserResponse;
import com.bhanu.movie_booking_system.entity.User;
import com.bhanu.movie_booking_system.exception.UserNotFoundException;
import com.bhanu.movie_booking_system.repository.UserRepository;
import com.bhanu.movie_booking_system.service.UserService;
import com.bhanu.movie_booking_system.transformer.UserTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log =
            LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    // =========================================================
    // GET ALL USERS
    // =========================================================

    @Override
    public List<UserResponse> getAllUsers() {

        log.info("Get all users request received");

        List<User> users =
                userRepository.findAll();

        log.debug(
                "Users retrieved from database. count={}",
                users.size()
        );

        List<UserResponse> responses =
                new ArrayList<>();

        for (User user : users) {

            responses.add(
                    UserTransformer.userToUserResponse(user)
            );
        }

        log.info(
                "All users fetched successfully. count={}",
                responses.size()
        );

        return responses;
    }


    // =========================================================
    // GET USER BY ID
    // =========================================================

    @Override
    public UserResponse getUserById(Long userId) {

        log.info(
                "Get user request received. userId={}",
                userId
        );

        User user =
                userRepository.findById(userId)
                        .orElseThrow(() -> {

                            log.warn(
                                    "User not found. userId={}",
                                    userId
                            );

                            return new UserNotFoundException(
                                    "User Not Found"
                            );
                        });

        log.info(
                "User fetched successfully. userId={}, userName={}",
                userId,
                user.getUserName()
        );

        return UserTransformer.userToUserResponse(user);
    }


    // =========================================================
    // UPDATE USER
    // =========================================================

    @Override
    public UserResponse updateUser(
            Long userId,
            UpdateUserRequest updateUserRequest) {

        log.info(
                "User update request received. userId={}",
                userId
        );

        User user =
                userRepository.findById(userId)
                        .orElseThrow(() -> {

                            log.warn(
                                    "User update failed. User not found. userId={}",
                                    userId
                            );

                            return new UserNotFoundException(
                                    "User Not Found"
                            );
                        });

        user.setUserName(
                updateUserRequest.getUserName()
        );

        user.setEmail(
                updateUserRequest.getEmail()
        );

        user.setPhoneNumber(
                updateUserRequest.getPhoneNumber()
        );

        User updatedUser =
                userRepository.save(user);

        log.info(
                "User updated successfully. userId={}, userName={}",
                updatedUser.getUserId(),
                updatedUser.getUserName()
        );

        return UserTransformer.userToUserResponse(
                updatedUser
        );
    }


    // =========================================================
    // DELETE USER
    // =========================================================

    @Override
    public void deleteUser(Long userId) {

        log.info(
                "User deletion request received. userId={}",
                userId
        );

        User user =
                userRepository.findById(userId)
                        .orElseThrow(() -> {

                            log.warn(
                                    "User deletion failed. User not found. userId={}",
                                    userId
                            );

                            return new UserNotFoundException(
                                    "User Not Found"
                            );
                        });

        userRepository.delete(user);

        log.info(
                "User deleted successfully. userId={}, userName={}",
                userId,
                user.getUserName()
        );
    }


    // =========================================================
    // GET PROFILE
    // =========================================================

    @Override
    public UserResponse getProfile(
            String username) {

        log.info(
                "Get user profile request received. userName={}",
                username
        );

        User user =
                userRepository.findByUserName(username)
                        .orElseThrow(() -> {

                            log.warn(
                                    "Profile lookup failed. User not found. userName={}",
                                    username
                            );

                            return new UserNotFoundException(
                                    "User Not Found"
                            );
                        });

        log.info(
                "User profile fetched successfully. userName={}",
                username
        );

        return UserTransformer.userToUserResponse(
                user
        );
    }


    // =========================================================
    // UPDATE PROFILE
    // =========================================================

    @Override
    public UserResponse updateProfile(
            String username,
            UpdateUserRequest updateUserRequest) {

        log.info(
                "Profile update request received. userName={}",
                username
        );

        User user =
                userRepository.findByUserName(username)
                        .orElseThrow(() -> {

                            log.warn(
                                    "Profile update failed. User not found. userName={}",
                                    username
                            );

                            return new UserNotFoundException(
                                    "User Not Found"
                            );
                        });

        user.setUserName(
                updateUserRequest.getUserName()
        );

        user.setEmail(
                updateUserRequest.getEmail()
        );

        user.setPhoneNumber(
                updateUserRequest.getPhoneNumber()
        );

        User updatedUser =
                userRepository.save(user);

        log.info(
                "User profile updated successfully. userId={}, userName={}",
                updatedUser.getUserId(),
                updatedUser.getUserName()
        );

        return UserTransformer.userToUserResponse(
                updatedUser
        );
    }


    // =========================================================
    // CHANGE PASSWORD
    // =========================================================

    @Override
    public void changePassword(
            String username,
            ChangePasswordRequest request) {

        log.info(
                "Password change request received. userName={}",
                username
        );

        User user =
                userRepository.findByUserName(username)
                        .orElseThrow(() -> {

                            log.warn(
                                    "Password change failed. User not found. userName={}",
                                    username
                            );

                            return new UserNotFoundException(
                                    "User Not Found"
                            );
                        });


        // Verify current password
        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPassword())) {

            log.warn(
                    "Password change failed. Current password is incorrect. userName={}",
                    username
            );

            throw new RuntimeException(
                    "Current Password is Incorrect"
            );
        }


        // Encode and save new password
        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        userRepository.save(user);


        log.info(
                "Password changed successfully. userName={}",
                username
        );
    }
}