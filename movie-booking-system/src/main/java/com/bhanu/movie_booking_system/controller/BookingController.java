package com.bhanu.movie_booking_system.controller;

import com.bhanu.movie_booking_system.dto.request.BookingRequest;
import com.bhanu.movie_booking_system.dto.response.BookingResponse;
import com.bhanu.movie_booking_system.entity.Enum.BookingStatus;
import com.bhanu.movie_booking_system.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public BookingResponse bookTickets(@Valid @RequestBody BookingRequest bookingRequest) {
        return bookingService.bookTickets(bookingRequest);
    }

    @GetMapping
    public List<BookingResponse> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/{bookingId}")
    public BookingResponse getBookingById(@PathVariable Long bookingId) {
        return bookingService.getBookingById(bookingId);
    }

    @DeleteMapping("/{bookingId}")
    public String cancelBooking(@PathVariable Long bookingId) {
        bookingService.cancelBooking(bookingId);
        return "Booking Cancelled Successfully";
    }
    @GetMapping("/status/{status}")
    public List<BookingResponse> getBookingsByStatus(
            @PathVariable BookingStatus status) {

        return bookingService.getBookingsByStatus(status);
    }
    @GetMapping("/my")
    public List<BookingResponse> getMyBookings() {
        return bookingService.getMyBookings();
    }
}