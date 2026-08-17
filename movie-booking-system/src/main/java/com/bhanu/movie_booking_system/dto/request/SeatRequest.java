package com.bhanu.movie_booking_system.dto.request;

import com.bhanu.movie_booking_system.entity.Enum.SeatType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeatRequest {

    @NotBlank(message = "Seat number is required")
    private String seatNumber;

    @NotNull(message = "Seat type is required")
    private SeatType seatType;

    @NotNull(message = "Screen ID is required")
    private Long screenId;
}