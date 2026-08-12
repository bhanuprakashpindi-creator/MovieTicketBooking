package com.bhanu.movie_booking_system.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {

    @NotNull(message = "User Id is required")
    private Long userId;

    @NotNull(message = "Show Id is required")
    private Long showId;

    @NotEmpty(message = "Please select at least one seat")
    private List<Long> showSeatIds;
}