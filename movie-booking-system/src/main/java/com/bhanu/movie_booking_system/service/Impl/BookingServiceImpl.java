package com.bhanu.movie_booking_system.service.Impl;

import com.bhanu.movie_booking_system.dto.request.BookingRequest;
import com.bhanu.movie_booking_system.dto.response.BookingResponse;
import com.bhanu.movie_booking_system.entity.Booking;
import com.bhanu.movie_booking_system.entity.Enum.BookingStatus;
import com.bhanu.movie_booking_system.entity.Enum.SeatStatus;
import com.bhanu.movie_booking_system.entity.Show;
import com.bhanu.movie_booking_system.entity.ShowSeat;
import com.bhanu.movie_booking_system.entity.User;
import com.bhanu.movie_booking_system.exception.BookingNotFoundException;
import com.bhanu.movie_booking_system.exception.SeatAlreadyBookedException;
import com.bhanu.movie_booking_system.exception.ShowNotFoundException;
import com.bhanu.movie_booking_system.exception.UserNotFoundException;
import com.bhanu.movie_booking_system.repository.BookingRepository;
import com.bhanu.movie_booking_system.repository.ShowRepository;
import com.bhanu.movie_booking_system.repository.ShowSeatRepository;
import com.bhanu.movie_booking_system.repository.UserRepository;
import com.bhanu.movie_booking_system.service.BookingService;
import com.bhanu.movie_booking_system.transformer.BookingTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private ShowSeatRepository showSeatRepository;

    @Autowired
    private BookingRepository bookingRepository;


    // =========================================================
    // BOOK TICKETS
    // =========================================================

    @Override
    @Transactional
    public BookingResponse bookTickets(BookingRequest bookingRequest) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String userName = authentication.getName();

        // Get logged-in user
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() ->
                        new UserNotFoundException("User Not Found"));


        // Get show
        Show show = showRepository.findById(bookingRequest.getShowId())
                .orElseThrow(() ->
                        new ShowNotFoundException("Show Not Found"));


        // Requested seats
        List<Long> requestedSeatIds =
                bookingRequest.getShowSeatIds();


        // Prevent duplicate seat IDs
        if (requestedSeatIds.size() !=
                requestedSeatIds.stream().distinct().count()) {

            throw new IllegalArgumentException(
                    "Duplicate seats are not allowed");
        }


        // Get seats with pessimistic locking
        List<ShowSeat> showSeats =
                showSeatRepository.findSeatsForBooking(
                        show.getShowId(),
                        requestedSeatIds
                );


        // Make sure all seats belong to this show
        if (showSeats.size() != requestedSeatIds.size()) {

            throw new IllegalArgumentException(
                    "One or more seats do not belong to this show");
        }


        // Check seat availability
        for (ShowSeat showSeat : showSeats) {

            if (showSeat.getSeatStatus() == SeatStatus.BOOKED) {

                throw new SeatAlreadyBookedException(
                        "Seat " +
                                showSeat.getSeat().getSeatNumber() +
                                " is already booked."
                );
            }
        }


        // Calculate total amount
        double totalAmount =
                show.getTicketPrice() * showSeats.size();


        // Convert request → Booking
        Booking booking =
                BookingTransformer.bookingRequestToBooking(
                        bookingRequest
                );


        // Set relationships
        booking.setUser(user);
        booking.setShow(show);
        booking.setShowSeats(showSeats);
        booking.setTotalAmount(totalAmount);


        // Mark seats as booked
        for (ShowSeat showSeat : showSeats) {

            showSeat.setSeatStatus(SeatStatus.BOOKED);
        }


        // Save seats
        showSeatRepository.saveAll(showSeats);


        // Save booking
        Booking savedBooking =
                bookingRepository.save(booking);


        return BookingTransformer.bookingToBookingResponse(
                savedBooking
        );
    }


    // =========================================================
    // GET BOOKINGS
    // =========================================================

    @Override
    public List<BookingResponse> getAllBookings() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin =
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(a ->
                                a.getAuthority().equals("ROLE_ADMIN"));


        List<Booking> bookings;

        if (isAdmin) {

            // ADMIN → all bookings
            bookings = bookingRepository.findAll();

        } else {

            // USER → only own bookings
            String userName = authentication.getName();

            User user = userRepository.findByUserName(userName)
                    .orElseThrow(() ->
                            new UserNotFoundException(
                                    "User Not Found"));

            bookings =
                    bookingRepository.findByUser(user);
        }


        return bookings.stream()
                .map(BookingTransformer::bookingToBookingResponse)
                .toList();
    }


    // =========================================================
    // GET BOOKING BY ID
    // =========================================================

    @Override
    public BookingResponse getBookingById(Long bookingId) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String userName = authentication.getName();

        Booking booking =
                bookingRepository.findById(bookingId)
                        .orElseThrow(() ->
                                new BookingNotFoundException(
                                        "Booking Not Found"));


        // ADMIN → can view any booking
        boolean isAdmin =
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(a ->
                                a.getAuthority().equals("ROLE_ADMIN"));


        if (!isAdmin &&
                !booking.getUser()
                        .getUserName()
                        .equals(userName)) {

            throw new BookingNotFoundException(
                    "Booking Not Found");
        }


        return BookingTransformer.bookingToBookingResponse(
                booking
        );
    }


    // =========================================================
    // CANCEL BOOKING
    // =========================================================

    @Override
    @Transactional
    public void cancelBooking(Long bookingId) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String userName = authentication.getName();

        boolean isAdmin =
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(a ->
                                a.getAuthority().equals("ROLE_ADMIN"));


        Booking booking =
                bookingRepository.findById(bookingId)
                        .orElseThrow(() ->
                                new BookingNotFoundException(
                                        "Booking Not Found"));


        // USER → only own booking
        if (!isAdmin &&
                !booking.getUser()
                        .getUserName()
                        .equals(userName)) {

            throw new BookingNotFoundException(
                    "Booking Not Found");
        }


        // Already cancelled
        if (booking.getBookingStatus() ==
                BookingStatus.CANCELLED) {

            throw new RuntimeException(
                    "Booking is already cancelled");
        }


        // Cancel booking
        booking.setBookingStatus(
                BookingStatus.CANCELLED
        );


        // Release seats
        List<ShowSeat> showSeats =
                booking.getShowSeats();

        for (ShowSeat showSeat : showSeats) {

            showSeat.setSeatStatus(
                    SeatStatus.AVAILABLE
            );
        }


        showSeatRepository.saveAll(showSeats);

        bookingRepository.save(booking);
    }


    // =========================================================
    // GET BOOKINGS BY STATUS
    // =========================================================

    @Override
    public List<BookingResponse> getBookingsByStatus(
            BookingStatus bookingStatus) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin =
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(a ->
                                a.getAuthority().equals("ROLE_ADMIN"));


        List<Booking> bookings;

        if (isAdmin) {

            // ADMIN → all bookings with this status
            bookings =
                    bookingRepository.findByBookingStatus(
                            bookingStatus
                    );

        } else {

            // USER → only own bookings with this status
            String userName = authentication.getName();

            User user = userRepository.findByUserName(userName)
                    .orElseThrow(() ->
                            new UserNotFoundException(
                                    "User Not Found"));

            bookings =
                    bookingRepository
                            .findByUserAndBookingStatus(
                                    user,
                                    bookingStatus
                            );
        }


        return bookings.stream()
                .map(BookingTransformer::bookingToBookingResponse)
                .toList();
    }
    @Override
    public List<BookingResponse> getMyBookings() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String userName = authentication.getName();

        User user = userRepository.findByUserName(userName)
                .orElseThrow(() ->
                        new UserNotFoundException("User Not Found"));

        List<Booking> bookings =
                bookingRepository.findByUser(user);

        return bookings.stream()
                .map(BookingTransformer::bookingToBookingResponse)
                .toList();
    }
}