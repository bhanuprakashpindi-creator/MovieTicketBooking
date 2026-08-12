package com.bhanu.movie_booking_system.dto.response;

import com.bhanu.movie_booking_system.entity.Enum.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {

    private Long bookingId;

    private String userName;

    private String movieTitle;

    private String theaterName;

    private String screenName;

    private LocalDateTime showDateTime;

    private List<String> seatNumbers;

    private Double totalAmount;

    private BookingStatus bookingStatus;
}