package com.bhanu.movie_booking_system.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShowRequest {

    @NotNull(message = "Movie Id is required")
    private Long movieId;

    @NotNull(message = "Screen Id is required")
    private Long screenId;

    @NotNull(message = "Show Date & Time is required")
    @Future(message = "Show must be scheduled in the future")
    private LocalDateTime showDateTime;

    @NotNull(message = "Ticket Price is required")
    @Positive(message = "Ticket Price must be greater than zero")
    private Double ticketPrice;
}