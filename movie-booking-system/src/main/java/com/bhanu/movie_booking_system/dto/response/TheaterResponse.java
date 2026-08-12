package com.bhanu.movie_booking_system.dto.response;

import com.bhanu.movie_booking_system.entity.Enum.State;
import com.bhanu.movie_booking_system.entity.Enum.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TheaterResponse {

    private Long theaterId;
    private String theaterName;
    private String city;
    private State state;
    private String address;
    private String contactNumber;
    private Status status;
}