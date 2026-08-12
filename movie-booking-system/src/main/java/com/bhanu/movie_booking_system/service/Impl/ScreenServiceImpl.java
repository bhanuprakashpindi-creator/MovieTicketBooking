package com.bhanu.movie_booking_system.service.Impl;

import com.bhanu.movie_booking_system.service.SeatService;
import com.bhanu.movie_booking_system.transformer.ScreenTransformer;
import com.bhanu.movie_booking_system.dto.request.ScreenRequest;
import com.bhanu.movie_booking_system.dto.response.ScreenResponse;
import com.bhanu.movie_booking_system.entity.Screen;
import com.bhanu.movie_booking_system.entity.Theater;
import com.bhanu.movie_booking_system.exception.ScreenNotFoundException;
import com.bhanu.movie_booking_system.exception.TheaterNotFoundException;
import com.bhanu.movie_booking_system.repository.ScreenRepository;
import com.bhanu.movie_booking_system.repository.TheaterRepository;
import com.bhanu.movie_booking_system.service.ScreenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScreenServiceImpl implements ScreenService {

    @Autowired
    private ScreenRepository screenRepository;
    @Autowired
    private SeatService seatService;
    @Autowired
    private TheaterRepository theaterRepository;

    @Override
    @Transactional
    public ScreenResponse addScreen(ScreenRequest screenRequest) {

        Theater theater = theaterRepository.findById(screenRequest.getTheaterId())
                .orElseThrow(() -> new TheaterNotFoundException("Theater Not Found"));

        Screen screen = ScreenTransformer.screenRequestToScreen(screenRequest);

        screen.setTheater(theater);

        Screen savedScreen = screenRepository.save(screen);

        seatService.generateSeats(savedScreen);

        return ScreenTransformer.screenToScreenResponse(savedScreen);
    }

    @Override
    public List<ScreenResponse> getAllScreens() {

        List<Screen> screens = screenRepository.findAll();

        List<ScreenResponse> responses = new ArrayList<>();

        for (Screen screen : screens) {
            responses.add(ScreenTransformer.screenToScreenResponse(screen));
        }

        return responses;
    }

    @Override
    public ScreenResponse getScreenById(Long screenId) {

        Screen screen = screenRepository.findById(screenId)
                .orElseThrow(() -> new ScreenNotFoundException("Screen Not Found"));

        return ScreenTransformer.screenToScreenResponse(screen);
    }

    @Override
    public ScreenResponse updateScreen(Long screenId, ScreenRequest screenRequest) {

        Screen screen = screenRepository.findById(screenId)
                .orElseThrow(() -> new ScreenNotFoundException("Screen Not Found"));

        Theater theater = theaterRepository.findById(screenRequest.getTheaterId())
                .orElseThrow(() -> new TheaterNotFoundException("Theater Not Found"));

        screen.setScreenName(screenRequest.getScreenName());
        screen.setScreenType(screenRequest.getScreenType());
        screen.setTotalSeats(screenRequest.getTotalSeats());
        screen.setTheater(theater);

        Screen updatedScreen = screenRepository.save(screen);

        return ScreenTransformer.screenToScreenResponse(updatedScreen);
    }

    @Override
    public void deleteScreen(Long screenId) {

        Screen screen = screenRepository.findById(screenId)
                .orElseThrow(() -> new ScreenNotFoundException("Screen Not Found"));

        screenRepository.delete(screen);
    }
}