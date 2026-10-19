package com.bhanu.movie_booking_system.exception;

import com.bhanu.movie_booking_system.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // -------------------- BAD REQUEST (400) --------------------

    @ExceptionHandler({
            UserAlreadyExistsException.class,
            MovieAlreadyExistsException.class,
            ShowAlreadyExistsException.class,
            SeatAlreadyBookedException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequestExceptions(
            RuntimeException ex,
            HttpServletRequest request) {

        ErrorResponse error = ErrorResponse.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(
                error,
                HttpStatus.BAD_REQUEST
        );
    }

    // -------------------- NOT FOUND (404) --------------------

    @ExceptionHandler({
            MovieNotFoundException.class,
            TheaterNotFoundException.class,
            ScreenNotFoundException.class,
            ShowNotFoundException.class,
            UserNotFoundException.class,
            BookingNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFoundExceptions(
            RuntimeException ex,
            HttpServletRequest request) {

        ErrorResponse error = ErrorResponse.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(
                error,
                HttpStatus.NOT_FOUND
        );
    }

    // -------------------- VALIDATION (400) --------------------

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error ->
                        error.getField() + ": "
                                + error.getDefaultMessage())
                .findFirst()
                .orElse("Invalid request");

        ErrorResponse error = ErrorResponse.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .message(message)
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(
                error,
                HttpStatus.BAD_REQUEST
        );
    }

    // -------------------- ILLEGAL ARGUMENT (400) --------------------

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        ErrorResponse error = ErrorResponse.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(
                error,
                HttpStatus.BAD_REQUEST
        );
    }

    // -------------------- CONFLICT (409) --------------------

    @ExceptionHandler({
            IllegalStateException.class,
            DataIntegrityViolationException.class
    })
    public ResponseEntity<ErrorResponse> handleConflictExceptions(
            Exception ex,
            HttpServletRequest request) {

        String message;

        if (ex instanceof DataIntegrityViolationException) {
            message = "Operation conflicts with existing data.";
        } else {
            message = ex.getMessage();
        }

        ErrorResponse error = ErrorResponse.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .message(message)
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(
                error,
                HttpStatus.CONFLICT
        );
    }

    // -------------------- INTERNAL SERVER ERROR (500) --------------------

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(
            Exception ex,
            HttpServletRequest request) {

        ErrorResponse error = ErrorResponse.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Something went wrong")
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(
                error,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}