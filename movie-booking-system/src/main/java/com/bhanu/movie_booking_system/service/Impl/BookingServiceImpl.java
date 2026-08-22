package com.bhanu.movie_booking_system.service.Impl;

import com.bhanu.movie_booking_system.dto.request.BookingRequest;
import com.bhanu.movie_booking_system.dto.response.BookingResponse;
import com.bhanu.movie_booking_system.entity.Booking;
import com.bhanu.movie_booking_system.entity.Enum.BookingStatus;
import com.bhanu.movie_booking_system.entity.Enum.SeatStatus;
import com.bhanu.movie_booking_system.entity.Enum.ShowStatus;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private static final Logger log =
            LoggerFactory.getLogger(BookingServiceImpl.class);

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

        log.info(
                "Booking request received. showId={}",
                bookingRequest.getShowId()
        );

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String userName = authentication.getName();

        // Get logged-in user
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() ->
                        new UserNotFoundException("User Not Found"));

        log.info(
                "Booking initiated by user={}",
                userName
        );


        // Get show
        Show show = showRepository.findById(
                        bookingRequest.getShowId()
                )
                .orElseThrow(() ->
                        new ShowNotFoundException("Show Not Found"));

        log.debug(
                "Show found. showId={}, movie={}",
                show.getShowId(),
                show.getMovie().getMovieTitle()
        );


        // Validate show status
        if (show.getShowStatus() != ShowStatus.SCHEDULED) {

            log.warn(
                    "Booking rejected. Invalid show status. showId={}, status={}",
                    show.getShowId(),
                    show.getShowStatus()
            );

            throw new IllegalArgumentException(
                    "Tickets cannot be booked for this show"
            );
        }


        // Validate show date and time
        if (!show.getShowDateTime().isAfter(LocalDateTime.now())) {

            log.warn(
                    "Booking rejected. Show has already started. showId={}, showDateTime={}",
                    show.getShowId(),
                    show.getShowDateTime()
            );

            throw new IllegalArgumentException(
                    "Cannot book a show that has already started"
            );
        }


        // Requested seats
        List<Long> requestedSeatIds =
                bookingRequest.getShowSeatIds();

        log.debug(
                "Requested seats. showId={}, seats={}",
                show.getShowId(),
                requestedSeatIds
        );


        // Prevent duplicate seat IDs
        if (requestedSeatIds.size() !=
                requestedSeatIds.stream().distinct().count()) {

            log.warn(
                    "Booking rejected. Duplicate seat IDs. user={}, showId={}, seats={}",
                    userName,
                    show.getShowId(),
                    requestedSeatIds
            );

            throw new IllegalArgumentException(
                    "Duplicate seats are not allowed"
            );
        }


        // Get seats with pessimistic locking
        List<ShowSeat> showSeats =
                showSeatRepository.findSeatsForBooking(
                        show.getShowId(),
                        requestedSeatIds
                );


        // Make sure all seats belong to this show
        if (showSeats.size() != requestedSeatIds.size()) {

            log.warn(
                    "Booking rejected. One or more seats do not belong to show. " +
                            "showId={}, requestedSeats={}",
                    show.getShowId(),
                    requestedSeatIds
            );

            throw new IllegalArgumentException(
                    "One or more seats do not belong to this show"
            );
        }


        // Check seat availability
        for (ShowSeat showSeat : showSeats) {

            if (showSeat.getSeatStatus() == SeatStatus.BOOKED) {

                log.warn(
                        "Booking rejected. Seat already booked. " +
                                "seatNumber={}, showId={}, user={}",
                        showSeat.getSeat().getSeatNumber(),
                        show.getShowId(),
                        userName
                );

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

        log.debug(
                "Booking amount calculated. showId={}, seats={}, totalAmount={}",
                show.getShowId(),
                showSeats.size(),
                totalAmount
        );


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

            showSeat.setSeatStatus(
                    SeatStatus.BOOKED
            );
        }


        // Save seats
        showSeatRepository.saveAll(showSeats);


        // Save booking
        Booking savedBooking =
                bookingRepository.save(booking);


        log.info(
                "Booking successful. bookingId={}, user={}, showId={}, seats={}, totalAmount={}",
                savedBooking.getBookingId(),
                userName,
                show.getShowId(),
                requestedSeatIds,
                totalAmount
        );


        return BookingTransformer.bookingToBookingResponse(
                savedBooking
        );
    }


    // =========================================================
    // GET ALL BOOKINGS
    // =========================================================

    @Override
    public List<BookingResponse> getAllBookings() {

        log.info("Get all bookings request received");

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin =
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(a ->
                                a.getAuthority().equals("ROLE_ADMIN"));

        log.debug(
                "Fetching bookings. isAdmin={}",
                isAdmin
        );


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


        log.info(
                "Bookings fetched successfully. count={}, isAdmin={}",
                bookings.size(),
                isAdmin
        );


        return bookings.stream()
                .map(BookingTransformer::bookingToBookingResponse)
                .toList();
    }


    // =========================================================
    // GET BOOKING BY ID
    // =========================================================

    @Override
    public BookingResponse getBookingById(Long bookingId) {

        log.info(
                "Get booking request received. bookingId={}",
                bookingId
        );

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String userName = authentication.getName();

        Booking booking =
                bookingRepository.findById(bookingId)
                        .orElseThrow(() ->
                                new BookingNotFoundException(
                                        "Booking Not Found"));

        log.debug(
                "Booking found. bookingId={}, status={}",
                bookingId,
                booking.getBookingStatus()
        );


        // ADMIN → can view any booking
        boolean isAdmin =
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(a ->
                                a.getAuthority().equals("ROLE_ADMIN"));


        // USER → only own booking
        if (!isAdmin &&
                !booking.getUser()
                        .getUserName()
                        .equals(userName)) {

            log.warn(
                    "Unauthorized booking access attempt. bookingId={}, user={}",
                    bookingId,
                    userName
            );

            throw new BookingNotFoundException(
                    "Booking Not Found"
            );
        }


        log.info(
                "Booking fetched successfully. bookingId={}",
                bookingId
        );


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

        log.info(
                "Booking cancellation request received. bookingId={}, user={}",
                bookingId,
                userName
        );


        boolean isAdmin =
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(a ->
                                a.getAuthority().equals("ROLE_ADMIN"));


        // Find booking
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

            log.warn(
                    "Unauthorized cancellation attempt. bookingId={}, user={}",
                    bookingId,
                    userName
            );

            throw new BookingNotFoundException(
                    "Booking Not Found"
            );
        }


        // Already cancelled
        if (booking.getBookingStatus() ==
                BookingStatus.CANCELLED) {

            log.warn(
                    "Cancellation failed. Booking already cancelled. bookingId={}",
                    bookingId
            );

            throw new IllegalArgumentException(
                    "Booking is already cancelled"
            );
        }


        // Check whether show has already started
        if (!booking.getShow()
                .getShowDateTime()
                .isAfter(LocalDateTime.now())) {

            log.warn(
                    "Cancellation rejected. Show has already started. " +
                            "bookingId={}, showId={}",
                    bookingId,
                    booking.getShow().getShowId()
            );

            throw new IllegalArgumentException(
                    "Booking cannot be cancelled after the show has started"
            );
        }


        // Check show status
        ShowStatus showStatus =
                booking.getShow().getShowStatus();

        if (showStatus == ShowStatus.CANCELLED ||
                showStatus == ShowStatus.COMPLETED) {

            log.warn(
                    "Cancellation rejected due to show status. " +
                            "bookingId={}, showStatus={}",
                    bookingId,
                    showStatus
            );

            throw new IllegalArgumentException(
                    "Booking cannot be cancelled for this show"
            );
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


        // Save released seats
        showSeatRepository.saveAll(showSeats);


        // Save cancelled booking
        bookingRepository.save(booking);


        log.info(
                "Booking cancelled successfully. bookingId={}, user={}, seatsReleased={}",
                bookingId,
                userName,
                showSeats.size()
        );
    }


    // =========================================================
    // GET BOOKINGS BY STATUS
    // =========================================================

    @Override
    public List<BookingResponse> getBookingsByStatus(
            BookingStatus bookingStatus) {

        log.info(
                "Get bookings by status request received. status={}",
                bookingStatus
        );


        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin =
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(a ->
                                a.getAuthority().equals("ROLE_ADMIN"));


        log.debug(
                "Fetching bookings by status. status={}, isAdmin={}",
                bookingStatus,
                isAdmin
        );


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


        log.info(
                "Bookings fetched by status. status={}, count={}, isAdmin={}",
                bookingStatus,
                bookings.size(),
                isAdmin
        );


        return bookings.stream()
                .map(BookingTransformer::bookingToBookingResponse)
                .toList();
    }


    // =========================================================
    // GET MY BOOKINGS
    // =========================================================

    @Override
    public List<BookingResponse> getMyBookings() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String userName = authentication.getName();

        log.info(
                "Get my bookings request received. user={}",
                userName
        );


        User user = userRepository.findByUserName(userName)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User Not Found"));


        List<Booking> bookings =
                bookingRepository.findByUser(user);


        log.info(
                "User bookings fetched successfully. user={}, count={}",
                userName,
                bookings.size()
        );


        return bookings.stream()
                .map(BookingTransformer::bookingToBookingResponse)
                .toList();
    }
}