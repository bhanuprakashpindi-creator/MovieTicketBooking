    package com.bhanu.movie_booking_system.service.Impl;

    import com.bhanu.movie_booking_system.dto.request.UserRequest;
    import com.bhanu.movie_booking_system.dto.response.UserResponse;
    import com.bhanu.movie_booking_system.entity.Enum.Role;
    import com.bhanu.movie_booking_system.entity.User;
    import com.bhanu.movie_booking_system.exception.UserAlreadyExistsException;
    import com.bhanu.movie_booking_system.repository.UserRepository;
    import com.bhanu.movie_booking_system.service.JWTService;
    import com.bhanu.movie_booking_system.service.UserService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.stereotype.Service;

    @Service
    public class UserServiceImpl implements UserService {
        private BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder(12);
        @Autowired
        private UserRepository userRepository;
        @Autowired
        private AuthenticationManager authManager;
        @Autowired
        private JWTService jwtService;
        @Override
        public UserResponse registerUser(UserRequest userRequest){
            //exception check
            if(userRepository.existsByEmail(userRequest.getEmail()))
            {
                throw  new UserAlreadyExistsException("Email Already Exists");
            }
            if(userRepository.existsByPhoneNumber(userRequest.getPhoneNumber()))
            {
                throw  new UserAlreadyExistsException("Phone Number Already Exists");
            }
            //dto --> databse

            User user=new User();
            user.setName(userRequest.getName());
            user.setEmail(userRequest.getEmail());
            user.setPassword(bCryptPasswordEncoder.encode(userRequest.getPassword()));
            user.setPhoneNumber(userRequest.getPhoneNumber());
            user.setRole(Role.USER);

            // saving into repository
            User newUser=userRepository.save(user);

            //entity --> userResponse

            UserResponse userResponse=new UserResponse();
            userResponse.setEmail(newUser.getEmail());
            userResponse.setName(newUser.getName());
            userResponse.setPhoneNumber(newUser.getPhoneNumber());
            userResponse.setRole(newUser.getRole());
            userResponse.setUserId(newUser.getUserId());

            return userResponse;
        }

        @Override
        public String verify(User user) {
            Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getName(),user.getPassword()));
            if(authentication.isAuthenticated())
                return jwtService.generateToken(user.getName());
            return "fail";
        }
    }
