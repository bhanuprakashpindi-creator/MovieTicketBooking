package com.bhanu.movie_booking_system.dto.response;

import com.bhanu.movie_booking_system.entity.Enum.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long userId;
    private String name;
    private String email;
    private String phoneNumber;
    private Role role;
}
