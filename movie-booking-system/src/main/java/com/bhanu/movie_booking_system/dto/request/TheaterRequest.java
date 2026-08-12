package com.bhanu.movie_booking_system.dto.request;

import com.bhanu.movie_booking_system.entity.Enum.State;
import com.bhanu.movie_booking_system.entity.Enum.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TheaterRequest {

    @NotBlank(message = "Theater name is required")
    private String theaterName;

    @NotBlank(message = "City is required")
    private String city;

    @NotNull(message = "State is required")
    private State state;

    @NotBlank(message = "Address is required")
    private String address;

    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Contact number must contain exactly 10 digits"
    )
    private String contactNumber;

    @NotNull(message = "Status is required")
    private Status status;
}