package com.bhanu.movie_booking_system.transformer;

import com.bhanu.movie_booking_system.dto.request.ScreenRequest;
import com.bhanu.movie_booking_system.dto.response.ScreenResponse;
import com.bhanu.movie_booking_system.entity.Screen;

public class ScreenTransformer {

    public static Screen screenRequestToScreen(ScreenRequest screenRequest) {

        Screen screen = new Screen();

        screen.setScreenName(screenRequest.getScreenName());
        screen.setScreenType(screenRequest.getScreenType());
        screen.setTotalSeats(screenRequest.getTotalSeats());

        return screen;
    }

    public static ScreenResponse screenToScreenResponse(Screen screen) {

        ScreenResponse response = new ScreenResponse();

        response.setScreenId(screen.getScreenId());
        response.setScreenName(screen.getScreenName());
        response.setScreenType(screen.getScreenType());
        response.setTotalSeats(screen.getTotalSeats());

        response.setTheaterId(screen.getTheater().getTheaterId());
        response.setTheaterName(screen.getTheater().getTheaterName());

        return response;
    }
}