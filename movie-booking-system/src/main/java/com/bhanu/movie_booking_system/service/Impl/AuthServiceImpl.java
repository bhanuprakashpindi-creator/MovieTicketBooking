package com.bhanu.movie_booking_system.service.Impl;

import com.bhanu.movie_booking_system.dto.request.LoginRequest;
import com.bhanu.movie_booking_system.dto.request.UserRequest;
import com.bhanu.movie_booking_system.dto.response.LoginResponse;
import com.bhanu.movie_booking_system.dto.response.UserResponse;
import com.bhanu.movie_booking_system.entity.Enum.Role;
import com.bhanu.movie_booking_system.entity.User;
import com.bhanu.movie_booking_system.exception.UserAlreadyExistsException;
import com.bhanu.movie_booking_system.repository.UserRepository;
import com.bhanu.movie_booking_system.service.AuthService;
import com.bhanu.movie_booking_system.service.JWTService;
import com.bhanu.movie_booking_system.transformer.UserTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger log =
            LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    // =========================================================
    // REGISTER
    // =========================================================

    @Override
    public UserResponse register(UserRequest userRequest) {

        log.info(
                "User registration request received. userName={}",
                userRequest.getUserName()
        );

        // Check email
        if (userRepository.existsByEmail(userRequest.getEmail())) {

            log.warn(
                    "Registration failed. Email already exists. email={}",
                    userRequest.getEmail()
            );

            throw new UserAlreadyExistsException(
                    "Email Already Exists"
            );
        }


        // Check phone number
        if (userRepository.existsByPhoneNumber(
                userRequest.getPhoneNumber())) {

            log.warn(
                    "Registration failed. Phone number already exists."
            );

            throw new UserAlreadyExistsException(
                    "Phone Number Already Exists"
            );
        }


        // Convert request → User
        User user =
                UserTransformer.userRequestToUser(userRequest);


        // Encrypt password
        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );


        // Default role
        user.setRole(Role.USER);


        // Save user
        User savedUser =
                userRepository.save(user);


        log.info(
                "User registered successfully. userId={}, userName={}",
                savedUser.getUserId(),
                savedUser.getUserName()
        );


        return UserTransformer.userToUserResponse(
                savedUser
        );
    }


    // =========================================================
    // LOGIN
    // =========================================================

    @Override
    public LoginResponse login(LoginRequest loginRequest) {

        log.info(
                "Login request received. userName={}",
                loginRequest.getUserName()
        );


        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUserName(),
                            loginRequest.getPassword()
                    )
            );

        } catch (Exception ex) {

            log.warn(
                    "Login failed. userName={}",
                    loginRequest.getUserName()
            );

            throw ex;
        }


        UserDetails userDetails =
                userDetailsService.loadUserByUsername(
                        loginRequest.getUserName()
                );

        String token =
                jwtService.generateToken(userDetails);


        log.info(
                "Login successful. userName={}",
                loginRequest.getUserName()
        );


        // IMPORTANT:
        // Do NOT log the JWT token.


        return new LoginResponse(token);
    }
}