package com.bhanu.movie_booking_system.service.Impl;

import com.bhanu.movie_booking_system.dto.request.PaymentRequest;
import com.bhanu.movie_booking_system.dto.response.PaymentResponse;
import com.bhanu.movie_booking_system.entity.Booking;
import com.bhanu.movie_booking_system.entity.Payment;
import com.bhanu.movie_booking_system.entity.Enum.BookingStatus;
import com.bhanu.movie_booking_system.entity.Enum.PaymentStatus;
import com.bhanu.movie_booking_system.exception.BookingNotFoundException;
import com.bhanu.movie_booking_system.repository.BookingRepository;
import com.bhanu.movie_booking_system.repository.PaymentRepository;
import com.bhanu.movie_booking_system.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final Logger log =
            LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BookingRepository bookingRepository;


    // =========================================================
    // MAKE PAYMENT
    // =========================================================

    @Override
    @Transactional
    public PaymentResponse makePayment(
            PaymentRequest paymentRequest) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String userName =
                authentication.getName();

        log.info(
                "Payment request received. bookingId={}, user={}",
                paymentRequest.getBookingId(),
                userName
        );


        // 1. Find booking

        Booking booking =
                bookingRepository.findById(
                                paymentRequest.getBookingId()
                        )
                        .orElseThrow(() -> {

                            log.warn(
                                    "Payment failed. Booking not found. bookingId={}",
                                    paymentRequest.getBookingId()
                            );

                            return new BookingNotFoundException(
                                    "Booking Not Found"
                            );
                        });


        log.debug(
                "Booking found for payment. bookingId={}, status={}",
                booking.getBookingId(),
                booking.getBookingStatus()
        );


        // 2. Check booking ownership

        boolean isAdmin =
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(a ->
                                a.getAuthority()
                                        .equals("ROLE_ADMIN"));


        if (!isAdmin &&
                !booking.getUser()
                        .getUserName()
                        .equals(userName)) {

            log.warn(
                    "Unauthorized payment attempt. bookingId={}, user={}",
                    booking.getBookingId(),
                    userName
            );

            throw new BookingNotFoundException(
                    "Booking Not Found"
            );
        }


        // 3. Check booking status

        if (booking.getBookingStatus() ==
                BookingStatus.CANCELLED) {

            log.warn(
                    "Payment rejected. Booking is cancelled. bookingId={}",
                    booking.getBookingId()
            );

            throw new IllegalArgumentException(
                    "Cannot make payment for a cancelled booking"
            );
        }


        if (booking.getBookingStatus() ==
                BookingStatus.CONFIRMED) {

            log.warn(
                    "Payment rejected. Booking already confirmed. bookingId={}",
                    booking.getBookingId()
            );

            throw new IllegalArgumentException(
                    "Booking is already confirmed"
            );
        }


        if (booking.getBookingStatus() ==
                BookingStatus.FAILED) {

            log.warn(
                    "Payment rejected. Booking has already failed. bookingId={}",
                    booking.getBookingId()
            );

            throw new IllegalArgumentException(
                    "Booking has already failed"
            );
        }


        // 4. Prevent duplicate payment

        if (paymentRepository.existsByBooking_BookingId(
                booking.getBookingId())) {

            log.warn(
                    "Payment rejected. Payment already exists. bookingId={}",
                    booking.getBookingId()
            );

            throw new IllegalArgumentException(
                    "Payment already exists for this booking"
            );
        }


        // 5. Create payment

        Payment payment =
                new Payment();

        payment.setBooking(booking);

        payment.setAmount(
                booking.getTotalAmount()
        );

        payment.setPaymentMethod(
                paymentRequest.getPaymentMethod()
        );

        payment.setPaymentDateTime(
                LocalDateTime.now()
        );

        payment.setTransactionId(
                "TXN-" +
                        UUID.randomUUID()
                                .toString()
                                .substring(0, 8)
                                .toUpperCase()
        );


        // =====================================================
        // MOCK PAYMENT
        // =====================================================

        payment.setPaymentStatus(
                PaymentStatus.SUCCESS
        );


        log.debug(
                "Payment created. bookingId={}, paymentMethod={}, status={}",
                booking.getBookingId(),
                payment.getPaymentMethod(),
                payment.getPaymentStatus()
        );


        // 6. Confirm booking

        booking.setBookingStatus(
                BookingStatus.CONFIRMED
        );


        // 7. Save booking

        bookingRepository.save(booking);


        // 8. Save payment

        Payment savedPayment =
                paymentRepository.save(payment);


        log.info(
                "Payment successful. paymentId={}, bookingId={}, transactionId={}, status={}",
                savedPayment.getPaymentId(),
                booking.getBookingId(),
                savedPayment.getTransactionId(),
                savedPayment.getPaymentStatus()
        );


        // 9. Convert entity → response

        return paymentToPaymentResponse(
                savedPayment
        );
    }


    // =========================================================
    // GET PAYMENT BY ID
    // =========================================================

    @Override
    public PaymentResponse getPaymentById(
            Long paymentId) {

        log.info(
                "Get payment request received. paymentId={}",
                paymentId
        );


        Payment payment =
                paymentRepository.findById(paymentId)
                        .orElseThrow(() -> {

                            log.warn(
                                    "Payment not found. paymentId={}",
                                    paymentId
                            );

                            return new RuntimeException(
                                    "Payment Not Found"
                            );
                        });


        log.info(
                "Payment fetched successfully. paymentId={}, status={}",
                paymentId,
                payment.getPaymentStatus()
        );


        return paymentToPaymentResponse(
                payment
        );
    }


    // =========================================================
    // GET PAYMENT BY BOOKING ID
    // =========================================================

    @Override
    public PaymentResponse getPaymentByBookingId(
            Long bookingId) {

        log.info(
                "Get payment by booking request received. bookingId={}",
                bookingId
        );


        Payment payment =
                paymentRepository
                        .findByBooking_BookingId(
                                bookingId
                        )
                        .orElseThrow(() -> {

                            log.warn(
                                    "Payment not found for booking. bookingId={}",
                                    bookingId
                            );

                            return new RuntimeException(
                                    "Payment Not Found"
                            );
                        });


        log.info(
                "Payment fetched for booking. bookingId={}, paymentId={}",
                bookingId,
                payment.getPaymentId()
        );


        return paymentToPaymentResponse(
                payment
        );
    }


    // =========================================================
    // GET PAYMENTS BY STATUS
    // =========================================================

    @Override
    public List<PaymentResponse> getPaymentsByStatus(
            PaymentStatus paymentStatus) {

        log.info(
                "Get payments by status request received. status={}",
                paymentStatus
        );


        List<PaymentResponse> payments =
                paymentRepository
                        .findByPaymentStatus(
                                paymentStatus
                        )
                        .stream()
                        .map(this::paymentToPaymentResponse)
                        .toList();


        log.info(
                "Payments fetched by status. status={}, count={}",
                paymentStatus,
                payments.size()
        );


        return payments;
    }


    // =========================================================
    // TRANSFORMER
    // =========================================================

    private PaymentResponse paymentToPaymentResponse(
            Payment payment) {

        PaymentResponse response =
                new PaymentResponse();

        response.setPaymentId(
                payment.getPaymentId()
        );

        response.setBookingId(
                payment.getBooking()
                        .getBookingId()
        );

        response.setTransactionId(
                payment.getTransactionId()
        );

        response.setAmount(
                payment.getAmount()
        );

        response.setPaymentMethod(
                payment.getPaymentMethod()
        );

        response.setPaymentStatus(
                payment.getPaymentStatus()
        );

        response.setPaymentDateTime(
                payment.getPaymentDateTime()
        );

        return response;
    }
}