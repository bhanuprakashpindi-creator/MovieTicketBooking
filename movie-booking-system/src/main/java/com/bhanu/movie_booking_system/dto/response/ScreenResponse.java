package com.bhanu.movie_booking_system.dto.response;

import com.bhanu.movie_booking_system.entity.Enum.ScreenType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScreenResponse {

    private Long screenId;
    private String screenName;
    private ScreenType screenType;
    private Integer totalSeats;

    private Long theaterId;
    private String theaterName;
}