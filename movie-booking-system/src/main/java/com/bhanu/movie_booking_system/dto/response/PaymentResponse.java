package com.bhanu.movie_booking_system.dto.response;

import com.bhanu.movie_booking_system.entity.Enum.PaymentMethod;
import com.bhanu.movie_booking_system.entity.Enum.PaymentStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private Long paymentId;

    private Long bookingId;

    private String transactionId;

    private Double amount;

    private PaymentMethod paymentMethod;

    private PaymentStatus paymentStatus;

    private LocalDateTime paymentDateTime;
}