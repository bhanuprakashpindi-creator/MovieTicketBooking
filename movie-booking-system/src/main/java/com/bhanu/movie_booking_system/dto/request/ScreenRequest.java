package com.bhanu.movie_booking_system.dto.request;

import com.bhanu.movie_booking_system.entity.Enum.ScreenType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScreenRequest {

    @NotBlank(message = "Screen name is required")
    private String screenName;

    @NotNull(message = "Screen type is required")
    private ScreenType screenType;

    @NotNull(message = "Total seats are required")
    @Positive(message = "Total seats must be greater than zero")
    private Integer totalSeats;

    @NotNull(message = "Theater Id is required")
    private Long theaterId;
}