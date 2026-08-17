package com.bhanu.movie_booking_system.transformer;

import com.bhanu.movie_booking_system.dto.request.SeatRequest;
import com.bhanu.movie_booking_system.dto.response.SeatResponse;
import com.bhanu.movie_booking_system.entity.Screen;
import com.bhanu.movie_booking_system.entity.Seat;

public class SeatTransformer {

    public static Seat seatRequestToSeat(SeatRequest seatRequest, Screen screen) {

        Seat seat = new Seat();

        seat.setSeatNumber(seatRequest.getSeatNumber());
        seat.setSeatType(seatRequest.getSeatType());
        seat.setScreen(screen);

        return seat;
    }

    public static SeatResponse seatToSeatResponse(Seat seat) {

        SeatResponse seatResponse = new SeatResponse();

        seatResponse.setSeatId(seat.getSeatId());
        seatResponse.setSeatNumber(seat.getSeatNumber());
        seatResponse.setSeatType(seat.getSeatType());
        seatResponse.setScreenId(seat.getScreen().getScreenId());

        return seatResponse;
    }
}