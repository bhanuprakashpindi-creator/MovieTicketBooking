package com.bhanu.movie_booking_system.service;

import com.bhanu.movie_booking_system.dto.request.ShowRequest;
import com.bhanu.movie_booking_system.dto.response.ShowResponse;

import java.time.LocalDate;
import java.util.List;

public interface ShowService {

    ShowResponse addShow(ShowRequest showRequest);

    List<ShowResponse> getAllShows();

    ShowResponse getShowById(Long showId);

    ShowResponse updateShow(Long showId, ShowRequest showRequest);

    void deleteShow(Long showId);

    // Search / Filtering
    List<ShowResponse> getShowsByMovie(Long movieId);

    List<ShowResponse> getShowsByDate(LocalDate date);

    List<ShowResponse> getShowsByTheater(Long theaterId);

    List<ShowResponse> getShowsByMovieAndDate(
            Long movieId,
            LocalDate date
    );

    List<ShowResponse> getShowsByTheaterAndDate(
            Long theaterId,
            LocalDate date
    );

    List<ShowResponse> getShowsByMovieAndTheaterAndDate(
            Long movieId,
            Long theaterId,
            LocalDate date
    );

}