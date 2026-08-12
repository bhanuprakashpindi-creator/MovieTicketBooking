package com.bhanu.movie_booking_system.exception;

public class SeatAlreadyBookedException extends RuntimeException{
    public SeatAlreadyBookedException(String message)
    {
        super(message);
    }
}
