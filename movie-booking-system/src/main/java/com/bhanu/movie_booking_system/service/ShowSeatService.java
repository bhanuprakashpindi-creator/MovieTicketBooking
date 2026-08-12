package com.bhanu.movie_booking_system.service;

import com.bhanu.movie_booking_system.entity.Screen;
import com.bhanu.movie_booking_system.entity.Show;

public interface ShowSeatService {

    void generateShowSeats(Show show, Screen screen);

}