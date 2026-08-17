package com.bhanu.movie_booking_system.dto.response;

import com.bhanu.movie_booking_system.entity.Enum.SeatType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeatResponse {

    private Long seatId;
    private String seatNumber;
    private SeatType seatType;
    private Long screenId;
}