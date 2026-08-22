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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScreenServiceImpl implements ScreenService {

    private static final Logger log =
            LoggerFactory.getLogger(ScreenServiceImpl.class);

    @Autowired
    private ScreenRepository screenRepository;

    @Autowired
    private SeatService seatService;

    @Autowired
    private TheaterRepository theaterRepository;


    // =========================================================
    // ADD SCREEN
    // =========================================================

    @Override
    @Transactional
    public ScreenResponse addScreen(ScreenRequest screenRequest) {

        log.info(
                "Screen creation request received. screenName={}, theaterId={}",
                screenRequest.getScreenName(),
                screenRequest.getTheaterId()
        );

        Theater theater =
                theaterRepository.findById(
                                screenRequest.getTheaterId()
                        )
                        .orElseThrow(() -> {

                            log.warn(
                                    "Screen creation failed. Theater not found. theaterId={}",
                                    screenRequest.getTheaterId()
                            );

                            return new TheaterNotFoundException(
                                    "Theater Not Found"
                            );
                        });

        Screen screen =
                ScreenTransformer.screenRequestToScreen(
                        screenRequest
                );

        screen.setTheater(theater);

        Screen savedScreen =
                screenRepository.save(screen);

        log.info(
                "Screen created successfully. screenId={}, screenName={}, theaterId={}",
                savedScreen.getScreenId(),
                savedScreen.getScreenName(),
                theater.getTheaterId()
        );

        // Generate seats for the screen
        seatService.generateSeats(savedScreen);

        log.info(
                "Seats generated successfully for screen. screenId={}, totalSeats={}",
                savedScreen.getScreenId(),
                savedScreen.getTotalSeats()
        );

        return ScreenTransformer.screenToScreenResponse(
                savedScreen
        );
    }


    // =========================================================
    // GET ALL SCREENS
    // =========================================================

    @Override
    public List<ScreenResponse> getAllScreens() {

        log.info("Get all screens request received");

        List<Screen> screens =
                screenRepository.findAll();

        log.debug(
                "Screens retrieved from database. count={}",
                screens.size()
        );

        List<ScreenResponse> responses =
                new ArrayList<>();

        for (Screen screen : screens) {

            responses.add(
                    ScreenTransformer.screenToScreenResponse(
                            screen
                    )
            );
        }

        log.info(
                "All screens fetched successfully. count={}",
                responses.size()
        );

        return responses;
    }


    // =========================================================
    // GET SCREEN BY ID
    // =========================================================

    @Override
    public ScreenResponse getScreenById(Long screenId) {

        log.info(
                "Get screen request received. screenId={}",
                screenId
        );

        Screen screen =
                screenRepository.findById(screenId)
                        .orElseThrow(() -> {

                            log.warn(
                                    "Screen not found. screenId={}",
                                    screenId
                            );

                            return new ScreenNotFoundException(
                                    "Screen Not Found"
                            );
                        });

        log.info(
                "Screen fetched successfully. screenId={}, screenName={}",
                screenId,
                screen.getScreenName()
        );

        return ScreenTransformer.screenToScreenResponse(
                screen
        );
    }


    // =========================================================
    // UPDATE SCREEN
    // =========================================================

    @Override
    public ScreenResponse updateScreen(
            Long screenId,
            ScreenRequest screenRequest) {

        log.info(
                "Screen update request received. screenId={}, theaterId={}",
                screenId,
                screenRequest.getTheaterId()
        );

        Screen screen =
                screenRepository.findById(screenId)
                        .orElseThrow(() -> {

                            log.warn(
                                    "Screen update failed. Screen not found. screenId={}",
                                    screenId
                            );

                            return new ScreenNotFoundException(
                                    "Screen Not Found"
                            );
                        });

        Theater theater =
                theaterRepository.findById(
                                screenRequest.getTheaterId()
                        )
                        .orElseThrow(() -> {

                            log.warn(
                                    "Screen update failed. Theater not found. theaterId={}",
                                    screenRequest.getTheaterId()
                            );

                            return new TheaterNotFoundException(
                                    "Theater Not Found"
                            );
                        });

        screen.setScreenName(
                screenRequest.getScreenName()
        );

        screen.setScreenType(
                screenRequest.getScreenType()
        );

        screen.setTotalSeats(
                screenRequest.getTotalSeats()
        );

        screen.setTheater(
                theater
        );

        Screen updatedScreen =
                screenRepository.save(screen);

        log.info(
                "Screen updated successfully. screenId={}, screenName={}, theaterId={}",
                updatedScreen.getScreenId(),
                updatedScreen.getScreenName(),
                theater.getTheaterId()
        );

        return ScreenTransformer.screenToScreenResponse(
                updatedScreen
        );
    }


    // =========================================================
    // DELETE SCREEN
    // =========================================================

    @Override
    public void deleteScreen(Long screenId) {

        log.info(
                "Screen deletion request received. screenId={}",
                screenId
        );

        Screen screen =
                screenRepository.findById(screenId)
                        .orElseThrow(() -> {

                            log.warn(
                                    "Screen deletion failed. Screen not found. screenId={}",
                                    screenId
                            );

                            return new ScreenNotFoundException(
                                    "Screen Not Found"
                            );
                        });

        screenRepository.delete(screen);

        log.info(
                "Screen deleted successfully. screenId={}, screenName={}",
                screenId,
                screen.getScreenName()
        );
    }
}