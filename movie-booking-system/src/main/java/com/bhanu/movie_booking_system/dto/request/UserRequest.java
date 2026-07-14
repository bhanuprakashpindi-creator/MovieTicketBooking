package com.bhanu.movie_booking_system.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    @NotBlank(message = "Name Is Required")
    private String name;
    @NotBlank
    @Email(message = "Enter Vaild Email")
    private String email;
    @NotBlank(message = "Enter Password")
    @Size(min = 8,message = "Password Must Be Atleast 8 Characters")
    private String password;
    @NotBlank
    @Pattern(regexp = "^[0-9]{10}$",message = "Phone Number Must Contains  10 Digits")
    private String phoneNumber;
}
