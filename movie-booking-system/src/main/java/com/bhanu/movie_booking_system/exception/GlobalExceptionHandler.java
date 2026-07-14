package com.bhanu.movie_booking_system.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.bhanu.movie_booking_system.dto.response.ErrorResponse;

import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex)
    {
        return new ResponseEntity<>(ErrorResponse.builder().timeStamp(LocalDateTime.now()).message(ex.getMessage()).status(HttpStatus.BAD_REQUEST.value()).path(null).build(),HttpStatus.BAD_REQUEST);
    }


}
