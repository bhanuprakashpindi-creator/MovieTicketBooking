package com.bhanu.movie_booking_system.service;

import com.bhanu.movie_booking_system.dto.request.PaymentRequest;
import com.bhanu.movie_booking_system.dto.response.PaymentResponse;
import com.bhanu.movie_booking_system.entity.Enum.PaymentStatus;

import java.util.List;

public interface PaymentService {

    PaymentResponse makePayment(PaymentRequest paymentRequest);

    PaymentResponse getPaymentById(Long paymentId);

    PaymentResponse getPaymentByBookingId(Long bookingId);

    List<PaymentResponse> getPaymentsByStatus(PaymentStatus paymentStatus);
}