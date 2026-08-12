package com.bhanu.movie_booking_system.exception;

public class MovieNotFoundException extends RuntimeException{
    public MovieNotFoundException(String message)
    {
        super(message);
    }
}
