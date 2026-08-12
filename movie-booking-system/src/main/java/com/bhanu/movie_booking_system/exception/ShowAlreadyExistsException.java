package com.bhanu.movie_booking_system.exception;

public class ShowAlreadyExistsException extends RuntimeException {

    public ShowAlreadyExistsException(String message) {
        super(message);
    }
}