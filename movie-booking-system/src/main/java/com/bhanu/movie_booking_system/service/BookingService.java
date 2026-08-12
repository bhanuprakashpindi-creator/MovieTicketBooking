package com.bhanu.movie_booking_system.service;

import com.bhanu.movie_booking_system.dto.request.BookingRequest;
import com.bhanu.movie_booking_system.dto.response.BookingResponse;

import java.util.List;

public interface BookingService {

    BookingResponse bookTickets(BookingRequest bookingRequest);

    List<BookingResponse> getAllBookings();

    BookingResponse getBookingById(Long bookingId);

    void cancelBooking(Long bookingId);
}