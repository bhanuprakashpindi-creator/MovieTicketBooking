package com.bhanu.movie_booking_system.controller;

import com.bhanu.movie_booking_system.dto.request.PaymentRequest;
import com.bhanu.movie_booking_system.dto.response.PaymentResponse;
import com.bhanu.movie_booking_system.entity.Enum.PaymentStatus;
import com.bhanu.movie_booking_system.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // Make payment
    @PostMapping
    public PaymentResponse makePayment(
            @Valid @RequestBody PaymentRequest paymentRequest) {

        return paymentService.makePayment(paymentRequest);
    }

    // Get payment by ID
    @GetMapping("/{paymentId}")
    public PaymentResponse getPaymentById(
            @PathVariable Long paymentId) {

        return paymentService.getPaymentById(paymentId);
    }

    // Get payment by booking ID
    @GetMapping("/booking/{bookingId}")
    public PaymentResponse getPaymentByBookingId(
            @PathVariable Long bookingId) {

        return paymentService.getPaymentByBookingId(bookingId);
    }

    // Get payments by status
    @GetMapping("/status/{status}")
    public List<PaymentResponse> getPaymentsByStatus(
            @PathVariable PaymentStatus status) {

        return paymentService.getPaymentsByStatus(status);
    }
}