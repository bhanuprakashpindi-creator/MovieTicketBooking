package com.bhanu.movie_booking_system.service.Impl;

import com.bhanu.movie_booking_system.dto.request.TheaterRequest;
import com.bhanu.movie_booking_system.dto.response.TheaterResponse;
import com.bhanu.movie_booking_system.entity.Theater;
import com.bhanu.movie_booking_system.exception.TheaterNotFoundException;
import com.bhanu.movie_booking_system.repository.TheaterRepository;
import com.bhanu.movie_booking_system.service.TheaterService;
import com.bhanu.movie_booking_system.transformer.TheaterTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TheaterServiceImpl implements TheaterService {

    private static final Logger log =
            LoggerFactory.getLogger(TheaterServiceImpl.class);

    @Autowired
    private TheaterRepository theaterRepository;


    // =========================================================
    // ADD THEATER
    // =========================================================

    @Override
    public TheaterResponse addTheater(
            TheaterRequest theaterRequest) {

        log.info(
                "Theater creation request received. theaterName={}, city={}",
                theaterRequest.getTheaterName(),
                theaterRequest.getCity()
        );


        Theater theater =
                TheaterTransformer.theaterRequestToTheater(
                        theaterRequest
                );


        Theater savedTheater =
                theaterRepository.save(theater);


        log.info(
                "Theater created successfully. theaterId={}, theaterName={}, city={}",
                savedTheater.getTheaterId(),
                savedTheater.getTheaterName(),
                savedTheater.getCity()
        );


        return TheaterTransformer
                .theaterToTheaterResponse(savedTheater);
    }


    // =========================================================
    // GET ALL THEATERS
    // =========================================================

    @Override
    public List<TheaterResponse> getAllTheaters() {

        log.info(
                "Get all theaters request received"
        );


        List<TheaterResponse> theaters =
                theaterRepository.findAll()
                        .stream()
                        .map(
                                TheaterTransformer::
                                        theaterToTheaterResponse
                        )
                        .toList();


        log.info(
                "All theaters fetched successfully. count={}",
                theaters.size()
        );


        return theaters;
    }


    // =========================================================
    // GET THEATER BY ID
    // =========================================================

    @Override
    public TheaterResponse getTheaterById(
            Long theaterId) {

        log.info(
                "Get theater request received. theaterId={}",
                theaterId
        );


        Theater theater =
                theaterRepository.findById(theaterId)
                        .orElseThrow(() -> {

                            log.warn(
                                    "Theater not found. theaterId={}",
                                    theaterId
                            );

                            return new TheaterNotFoundException(
                                    "Theater Not Found"
                            );
                        });


        log.info(
                "Theater fetched successfully. theaterId={}, theaterName={}",
                theaterId,
                theater.getTheaterName()
        );


        return TheaterTransformer
                .theaterToTheaterResponse(theater);
    }


    // =========================================================
    // UPDATE THEATER
    // =========================================================

    @Override
    public TheaterResponse updateTheater(
            Long theaterId,
            TheaterRequest theaterRequest) {

        log.info(
                "Theater update request received. theaterId={}",
                theaterId
        );


        Theater theater =
                theaterRepository.findById(theaterId)
                        .orElseThrow(() -> {

                            log.warn(
                                    "Theater update failed. Theater not found. theaterId={}",
                                    theaterId
                            );

                            return new TheaterNotFoundException(
                                    "Theater Not Found"
                            );
                        });


        theater.setTheaterName(
                theaterRequest.getTheaterName()
        );

        theater.setCity(
                theaterRequest.getCity()
        );

        theater.setState(
                theaterRequest.getState()
        );

        theater.setAddress(
                theaterRequest.getAddress()
        );

        theater.setContactNumber(
                theaterRequest.getContactNumber()
        );

        theater.setStatus(
                theaterRequest.getStatus()
        );


        Theater updatedTheater =
                theaterRepository.save(theater);


        log.info(
                "Theater updated successfully. theaterId={}, theaterName={}, city={}",
                updatedTheater.getTheaterId(),
                updatedTheater.getTheaterName(),
                updatedTheater.getCity()
        );


        return TheaterTransformer
                .theaterToTheaterResponse(updatedTheater);
    }


    // =========================================================
    // DELETE THEATER
    // =========================================================

    @Override
    public void deleteTheater(
            Long theaterId) {

        log.info(
                "Theater deletion request received. theaterId={}",
                theaterId
        );


        Theater theater =
                theaterRepository.findById(theaterId)
                        .orElseThrow(() -> {

                            log.warn(
                                    "Theater deletion failed. Theater not found. theaterId={}",
                                    theaterId
                            );

                            return new TheaterNotFoundException(
                                    "Theater Not Found"
                            );
                        });


        theaterRepository.delete(theater);


        log.info(
                "Theater deleted successfully. theaterId={}, theaterName={}",
                theaterId,
                theater.getTheaterName()
        );
    }
}