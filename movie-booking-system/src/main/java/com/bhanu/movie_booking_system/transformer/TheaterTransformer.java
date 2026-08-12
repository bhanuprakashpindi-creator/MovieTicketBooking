package com.bhanu.movie_booking_system.transformer;

import com.bhanu.movie_booking_system.dto.request.TheaterRequest;
import com.bhanu.movie_booking_system.dto.response.TheaterResponse;
import com.bhanu.movie_booking_system.entity.Theater;

public class TheaterTransformer {

    public static Theater theaterRequestToTheater(TheaterRequest theaterRequest) {

        Theater theater = new Theater();

        theater.setTheaterName(theaterRequest.getTheaterName());
        theater.setCity(theaterRequest.getCity());
        theater.setState(theaterRequest.getState());
        theater.setAddress(theaterRequest.getAddress());
        theater.setContactNumber(theaterRequest.getContactNumber());
        theater.setStatus(theaterRequest.getStatus());

        return theater;
    }

    public static TheaterResponse theaterToTheaterResponse(Theater theater) {

        TheaterResponse theaterResponse = new TheaterResponse();

        theaterResponse.setTheaterId(theater.getTheaterId());
        theaterResponse.setTheaterName(theater.getTheaterName());
        theaterResponse.setCity(theater.getCity());
        theaterResponse.setState(theater.getState());
        theaterResponse.setAddress(theater.getAddress());
        theaterResponse.setContactNumber(theater.getContactNumber());
        theaterResponse.setStatus(theater.getStatus());

        return theaterResponse;
    }
}