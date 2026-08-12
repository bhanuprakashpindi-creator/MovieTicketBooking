package com.bhanu.movie_booking_system.dto.response;

import com.bhanu.movie_booking_system.entity.Enum.ShowStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShowResponse {

    private Long showId;

    private Long movieId;
    private String movieTitle;

    private Long screenId;
    private String screenName;

    private LocalDateTime showDateTime;

    private Double ticketPrice;

    private ShowStatus showStatus;
}