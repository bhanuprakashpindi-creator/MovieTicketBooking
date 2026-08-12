package com.bhanu.movie_booking_system.service.Impl;

import com.bhanu.movie_booking_system.dto.request.TheaterRequest;
import com.bhanu.movie_booking_system.dto.response.TheaterResponse;
import com.bhanu.movie_booking_system.entity.Theater;
import com.bhanu.movie_booking_system.repository.TheaterRepository;
import com.bhanu.movie_booking_system.service.TheaterService;
import com.bhanu.movie_booking_system.transformer.TheaterTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TheaterServiceImpl implements TheaterService {

    @Autowired
    private TheaterRepository theaterRepository;

    @Override
    public TheaterResponse addTheater(TheaterRequest theaterRequest) {

        Theater theater =
                TheaterTransformer.theaterRequestToTheater(theaterRequest);

        Theater savedTheater = theaterRepository.save(theater);

        return TheaterTransformer.theaterToTheaterResponse(savedTheater);
    }

    @Override
    public List<TheaterResponse> getAllTheaters() {

        return theaterRepository.findAll()
                .stream()
                .map(TheaterTransformer::theaterToTheaterResponse)
                .toList();
    }

    @Override
    public TheaterResponse getTheaterById(Long theaterId) {

        Theater theater = theaterRepository.findById(theaterId)
                .orElseThrow(() ->
                        new RuntimeException("Theater Not Found"));

        return TheaterTransformer.theaterToTheaterResponse(theater);
    }

    @Override
    public TheaterResponse updateTheater(Long theaterId,
                                         TheaterRequest theaterRequest) {

        Theater theater = theaterRepository.findById(theaterId)
                .orElseThrow(() ->
                        new RuntimeException("Theater Not Found"));

        theater.setTheaterName(theaterRequest.getTheaterName());
        theater.setCity(theaterRequest.getCity());
        theater.setState(theaterRequest.getState());
        theater.setAddress(theaterRequest.getAddress());
        theater.setContactNumber(theaterRequest.getContactNumber());
        theater.setStatus(theaterRequest.getStatus());

        Theater updatedTheater = theaterRepository.save(theater);

        return TheaterTransformer.theaterToTheaterResponse(updatedTheater);
    }

    @Override
    public void deleteTheater(Long theaterId) {

        Theater theater = theaterRepository.findById(theaterId)
                .orElseThrow(() ->
                        new RuntimeException("Theater Not Found"));

        theaterRepository.delete(theater);
    }
}