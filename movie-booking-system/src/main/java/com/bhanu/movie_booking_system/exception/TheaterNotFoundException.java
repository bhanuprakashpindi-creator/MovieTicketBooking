package com.bhanu.movie_booking_system.exception;

import com.bhanu.movie_booking_system.entity.Theater;

public class TheaterNotFoundException extends RuntimeException{
    public TheaterNotFoundException (String message)
    {
        super(message);
    }

}
