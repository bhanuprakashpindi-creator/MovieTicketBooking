package com.bhanu.movie_booking_system.service;

import com.bhanu.movie_booking_system.dto.request.TheaterRequest;
import com.bhanu.movie_booking_system.dto.response.TheaterResponse;

import java.util.List;

public interface TheaterService {

    TheaterResponse addTheater(TheaterRequest theaterRequest);

    List<TheaterResponse> getAllTheaters();

    TheaterResponse getTheaterById(Long theaterId);

    TheaterResponse updateTheater(Long theaterId,
                                  TheaterRequest theaterRequest);

    void deleteTheater(Long theaterId);
}