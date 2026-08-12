package com.bhanu.movie_booking_system.service;

import com.bhanu.movie_booking_system.dto.request.ScreenRequest;
import com.bhanu.movie_booking_system.dto.response.ScreenResponse;

import java.util.List;

public interface ScreenService {

    ScreenResponse addScreen(ScreenRequest screenRequest);

    List<ScreenResponse> getAllScreens();

    ScreenResponse getScreenById(Long screenId);

    ScreenResponse updateScreen(Long screenId, ScreenRequest screenRequest);

    void deleteScreen(Long screenId);
}