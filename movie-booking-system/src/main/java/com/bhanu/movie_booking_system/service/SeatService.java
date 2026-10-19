package com.bhanu.movie_booking_system.service;

import com.bhanu.movie_booking_system.dto.request.SeatRequest;
import com.bhanu.movie_booking_system.dto.response.SeatResponse;
import com.bhanu.movie_booking_system.entity.Screen;

import java.util.List;

public interface SeatService {

    SeatResponse addSeat(SeatRequest seatRequest);

    List<SeatResponse> getAllSeats();

    SeatResponse getSeatById(Long seatId);

    SeatResponse updateSeat(Long seatId, SeatRequest seatRequest);

    void deleteSeat(Long seatId);
    void generateSeats(Screen screen);
    List<SeatResponse> getSeatsByScreen(Long screenId);
}