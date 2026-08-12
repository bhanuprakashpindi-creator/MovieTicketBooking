package com.bhanu.movie_booking_system.service;

import com.bhanu.movie_booking_system.dto.request.ShowRequest;
import com.bhanu.movie_booking_system.dto.response.ShowResponse;

import java.util.List;

public interface ShowService {

    ShowResponse addShow(ShowRequest showRequest);

    List<ShowResponse> getAllShows();

    ShowResponse getShowById(Long showId);

    ShowResponse updateShow(Long showId, ShowRequest showRequest);

    void deleteShow(Long showId);
}