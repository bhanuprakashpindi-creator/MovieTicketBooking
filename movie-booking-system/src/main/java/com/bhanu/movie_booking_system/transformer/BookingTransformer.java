package com.bhanu.movie_booking_system.transformer;

import com.bhanu.movie_booking_system.dto.request.BookingRequest;
import com.bhanu.movie_booking_system.dto.response.BookingResponse;
import com.bhanu.movie_booking_system.entity.Booking;
import com.bhanu.movie_booking_system.entity.Enum.BookingStatus;
import com.bhanu.movie_booking_system.entity.ShowSeat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingTransformer {

    public static Booking bookingRequestToBooking(BookingRequest request) {

        Booking booking = new Booking();

        booking.setBookingDateTime(LocalDateTime.now());
        booking.setBookingStatus(BookingStatus.CONFIRMED);

        return booking;
    }

    public static BookingResponse bookingToBookingResponse(Booking booking) {

        BookingResponse response = new BookingResponse();

        response.setBookingId(booking.getBookingId());

        response.setUserName(booking.getUser().getUserName());

        response.setMovieTitle(
                booking.getShow().getMovie().getMovieTitle());

        response.setTheaterName(
                booking.getShow()
                        .getScreen()
                        .getTheater()
                        .getTheaterName());

        response.setScreenName(
                booking.getShow()
                        .getScreen()
                        .getScreenName());

        response.setShowDateTime(
                booking.getShow().getShowDateTime());

        List<String> seatNumbers = new ArrayList<>();

        for (ShowSeat showSeat : booking.getShowSeats()) {
            seatNumbers.add(showSeat.getSeat().getSeatNumber());
        }

        response.setSeatNumbers(seatNumbers);

        response.setTotalAmount(booking.getTotalAmount());

        response.setBookingStatus(booking.getBookingStatus());

        return response;
    }
}