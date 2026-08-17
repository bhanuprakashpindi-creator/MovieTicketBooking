package com.bhanu.movie_booking_system.dto.response;

import com.bhanu.movie_booking_system.entity.Enum.SeatStatus;
import com.bhanu.movie_booking_system.entity.Enum.SeatType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShowSeatResponse {

    private Long showSeatId;
    private Long seatId;
    private String seatNumber;
    private SeatType seatType;
    private SeatStatus seatStatus;
}