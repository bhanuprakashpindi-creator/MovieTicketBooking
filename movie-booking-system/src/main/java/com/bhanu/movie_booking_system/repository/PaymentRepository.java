package com.bhanu.movie_booking_system.repository;

import com.bhanu.movie_booking_system.entity.Payment;
import com.bhanu.movie_booking_system.entity.Enum.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByBooking_BookingId(Long bookingId);

    Optional<Payment> findByTransactionId(String transactionId);

    boolean existsByBooking_BookingId(Long bookingId);

    java.util.List<Payment> findByPaymentStatus(PaymentStatus paymentStatus);
}