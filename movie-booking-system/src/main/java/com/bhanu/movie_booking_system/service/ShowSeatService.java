package com.bhanu.movie_booking_system.service;

import com.bhanu.movie_booking_system.dto.response.ShowSeatResponse;
import com.bhanu.movie_booking_system.entity.Screen;
import com.bhanu.movie_booking_system.entity.Show;

import java.util.List;

public interface ShowSeatService {

    void generateShowSeats(Show show, Screen screen);
    List<ShowSeatResponse> getShowSeats(Long showId);
}