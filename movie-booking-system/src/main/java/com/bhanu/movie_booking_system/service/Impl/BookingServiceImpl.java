package com.bhanu.movie_booking_system.service.Impl;

import com.bhanu.movie_booking_system.dto.request.BookingRequest;
import com.bhanu.movie_booking_system.dto.response.BookingResponse;
import com.bhanu.movie_booking_system.entity.Booking;
import com.bhanu.movie_booking_system.entity.Enum.SeatStatus;
import com.bhanu.movie_booking_system.entity.Show;
import com.bhanu.movie_booking_system.entity.ShowSeat;
import com.bhanu.movie_booking_system.entity.User;
import com.bhanu.movie_booking_system.exception.SeatAlreadyBookedException;
import com.bhanu.movie_booking_system.exception.ShowNotFoundException;
import com.bhanu.movie_booking_system.exception.UserNotFoundException;
import com.bhanu.movie_booking_system.repository.BookingRepository;
import com.bhanu.movie_booking_system.repository.ShowRepository;
import com.bhanu.movie_booking_system.repository.ShowSeatRepository;
import com.bhanu.movie_booking_system.repository.UserRepository;
import com.bhanu.movie_booking_system.service.BookingService;
import com.bhanu.movie_booking_system.transformer.BookingTransformer;
import org.hibernate.annotations.NaturalId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BookingServiceImpl implements BookingService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ShowRepository showRepository;
    @Autowired
    private ShowSeatRepository showSeatRepository;
    @NaturalId
    private BookingRepository bookingRepository;

    @Override
    public BookingResponse bookTickets(BookingRequest bookingRequest) {

        //********************** Validation**********************************
        User user = userRepository.findById(bookingRequest.getUserId())
                .orElseThrow(() ->
                        new UserNotFoundException("User Not Found"));
        Show show = showRepository.findById(bookingRequest.getShowId())
                .orElseThrow(() ->
                        new ShowNotFoundException("Show Not Found"));
        List<ShowSeat> showSeats =
                showSeatRepository.findAllById(bookingRequest.getShowSeatIds());
        for (ShowSeat showSeat : showSeats) {

            if (showSeat.getSeatStatus() == SeatStatus.BOOKED) {
                throw new SeatAlreadyBookedException(
                        "Seat " +
                                showSeat.getSeat().getSeatNumber() +
                                " is already booked.");
            }
        }
        //***************************** Dto To Entity ***************************
        double totalAmount =
                show.getTicketPrice() * showSeats.size();
        Booking booking =
                BookingTransformer.bookingRequestToBooking(bookingRequest);
        booking.setUser(user);

        booking.setShow(show);

        booking.setShowSeats(showSeats);

        booking.setTotalAmount(totalAmount);

        for (ShowSeat showSeat : showSeats) {

            showSeat.setSeatStatus(SeatStatus.BOOKED);
        }
        showSeatRepository.saveAll(showSeats);
        Booking savedBooking = bookingRepository.save(booking);
        return BookingTransformer.bookingToBookingResponse(savedBooking);

    }

    @Override
    public List<BookingResponse> getAllBookings() {
        return List.of();
    }

    @Override
    public BookingResponse getBookingById(Long bookingId) {
        return null;
    }

    @Override
    public void cancelBooking(Long bookingId) {

    }
}
