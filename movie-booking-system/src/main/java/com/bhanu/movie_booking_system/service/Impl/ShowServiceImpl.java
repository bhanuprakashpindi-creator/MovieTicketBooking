package com.bhanu.movie_booking_system.service.Impl;

import com.bhanu.movie_booking_system.dto.request.ShowRequest;
import com.bhanu.movie_booking_system.dto.response.ShowResponse;
import com.bhanu.movie_booking_system.entity.Enum.ShowStatus;
import com.bhanu.movie_booking_system.entity.Movie;
import com.bhanu.movie_booking_system.entity.Screen;
import com.bhanu.movie_booking_system.entity.Show;
import com.bhanu.movie_booking_system.exception.MovieNotFoundException;
import com.bhanu.movie_booking_system.exception.ScreenNotFoundException;
import com.bhanu.movie_booking_system.exception.ShowNotFoundException;
import com.bhanu.movie_booking_system.repository.MovieRepository;
import com.bhanu.movie_booking_system.repository.ScreenRepository;
import com.bhanu.movie_booking_system.repository.ShowRepository;
import com.bhanu.movie_booking_system.service.ShowSeatService;
import com.bhanu.movie_booking_system.service.ShowService;
import com.bhanu.movie_booking_system.transformer.ShowTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShowServiceImpl implements ShowService {

    private static final Logger log =
            LoggerFactory.getLogger(ShowServiceImpl.class);

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private ScreenRepository screenRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ShowSeatService showSeatService;


    // =========================================================
    // ADD SHOW
    // =========================================================

    @Override
    @Transactional
    public ShowResponse addShow(ShowRequest showRequest) {

        log.info(
                "Show creation request received. movieId={}, screenId={}, showDateTime={}",
                showRequest.getMovieId(),
                showRequest.getScreenId(),
                showRequest.getShowDateTime()
        );

        Movie movie =
                movieRepository.findById(
                                showRequest.getMovieId()
                        )
                        .orElseThrow(() -> {

                            log.warn(
                                    "Show creation failed. Movie not found. movieId={}",
                                    showRequest.getMovieId()
                            );

                            return new MovieNotFoundException(
                                    "Movie Not Found"
                            );
                        });


        Screen screen =
                screenRepository.findById(
                                showRequest.getScreenId()
                        )
                        .orElseThrow(() -> {

                            log.warn(
                                    "Show creation failed. Screen not found. screenId={}",
                                    showRequest.getScreenId()
                            );

                            return new ScreenNotFoundException(
                                    "Screen Not Found"
                            );
                        });


        Show show =
                ShowTransformer.showRequestToShow(
                        showRequest
                );


        show.setMovie(movie);
        show.setScreen(screen);
        show.setShowStatus(
                ShowStatus.SCHEDULED
        );


        Show savedShow =
                showRepository.save(show);


        log.info(
                "Show created successfully. showId={}, movieId={}, screenId={}, showDateTime={}",
                savedShow.getShowId(),
                movie.getMovieId(),
                screen.getScreenId(),
                savedShow.getShowDateTime()
        );


        // Generate seats for this show
        showSeatService.generateShowSeats(
                savedShow,
                screen
        );


        log.info(
                "Show seats generated successfully. showId={}",
                savedShow.getShowId()
        );


        return ShowTransformer.showToShowResponse(
                savedShow
        );
    }


    // =========================================================
    // GET ALL SHOWS
    // =========================================================

    @Override
    public List<ShowResponse> getAllShows() {

        log.info(
                "Get all shows request received"
        );


        List<Show> shows =
                showRepository.findAll();


        log.debug(
                "Shows retrieved from database. count={}",
                shows.size()
        );


        List<ShowResponse> responses =
                new ArrayList<>();


        for (Show show : shows) {

            updateShowStatus(show);

            responses.add(
                    ShowTransformer.showToShowResponse(
                            show
                    )
            );
        }


        log.info(
                "All shows fetched successfully. count={}",
                responses.size()
        );


        return responses;
    }


    // =========================================================
    // GET SHOW BY ID
    // =========================================================

    @Override
    public ShowResponse getShowById(Long showId) {

        log.info(
                "Get show request received. showId={}",
                showId
        );


        Show show =
                showRepository.findById(showId)
                        .orElseThrow(() -> {

                            log.warn(
                                    "Show not found. showId={}",
                                    showId
                            );

                            return new ShowNotFoundException(
                                    "Show Not Found"
                            );
                        });


        updateShowStatus(show);


        log.info(
                "Show fetched successfully. showId={}",
                showId
        );


        return ShowTransformer.showToShowResponse(
                show
        );
    }


    // =========================================================
    // UPDATE SHOW
    // =========================================================

    @Override
    public ShowResponse updateShow(
            Long showId,
            ShowRequest showRequest) {

        log.info(
                "Show update request received. showId={}, movieId={}, screenId={}",
                showId,
                showRequest.getMovieId(),
                showRequest.getScreenId()
        );


        Show show =
                showRepository.findById(showId)
                        .orElseThrow(() -> {

                            log.warn(
                                    "Show update failed. Show not found. showId={}",
                                    showId
                            );

                            return new ShowNotFoundException(
                                    "Show Not Found"
                            );
                        });


        Movie movie =
                movieRepository.findById(
                                showRequest.getMovieId()
                        )
                        .orElseThrow(() -> {

                            log.warn(
                                    "Show update failed. Movie not found. movieId={}",
                                    showRequest.getMovieId()
                            );

                            return new MovieNotFoundException(
                                    "Movie Not Found"
                            );
                        });


        Screen screen =
                screenRepository.findById(
                                showRequest.getScreenId()
                        )
                        .orElseThrow(() -> {

                            log.warn(
                                    "Show update failed. Screen not found. screenId={}",
                                    showRequest.getScreenId()
                            );

                            return new ScreenNotFoundException(
                                    "Screen Not Found"
                            );
                        });


        show.setMovie(movie);
        show.setScreen(screen);

        show.setShowDateTime(
                showRequest.getShowDateTime()
        );

        show.setTicketPrice(
                showRequest.getTicketPrice()
        );

        /*
         * Recalculate status after changing the show date/time.
         */
        if (show.getShowDateTime().isBefore(LocalDateTime.now())) {

            show.setShowStatus(
                    ShowStatus.COMPLETED
            );

        } else {

            show.setShowStatus(
                    ShowStatus.SCHEDULED
            );
        }


        Show updatedShow =
                showRepository.save(show);


        log.info(
                "Show updated successfully. showId={}, movieId={}, screenId={}, showDateTime={}",
                updatedShow.getShowId(),
                movie.getMovieId(),
                screen.getScreenId(),
                updatedShow.getShowDateTime()
        );


        return ShowTransformer.showToShowResponse(
                updatedShow
        );
    }


    // =========================================================
    // DELETE SHOW
    // =========================================================

    @Override
    public void deleteShow(Long showId) {

        log.info(
                "Show deletion request received. showId={}",
                showId
        );


        Show show =
                showRepository.findById(showId)
                        .orElseThrow(() -> {

                            log.warn(
                                    "Show deletion failed. Show not found. showId={}",
                                    showId
                            );

                            return new ShowNotFoundException(
                                    "Show Not Found"
                            );
                        });


        showRepository.delete(show);


        log.info(
                "Show deleted successfully. showId={}",
                showId
        );
    }


    // =========================================================
    // GET SHOWS BY MOVIE
    // =========================================================

    @Override
    public List<ShowResponse> getShowsByMovie(
            Long movieId) {

        log.info(
                "Get shows by movie request received. movieId={}",
                movieId
        );


        movieRepository.findById(movieId)
                .orElseThrow(() -> {

                    log.warn(
                            "Movie not found while searching shows. movieId={}",
                            movieId
                    );

                    return new MovieNotFoundException(
                            "Movie Not Found"
                    );
                });


        List<Show> shows =
                showRepository.findByMovie_MovieId(
                        movieId
                );


        log.info(
                "Shows fetched by movie. movieId={}, count={}",
                movieId,
                shows.size()
        );


        shows.forEach(this::updateShowStatus);


        return shows.stream()
                .map(ShowTransformer::showToShowResponse)
                .toList();
    }


    // =========================================================
    // GET SHOWS BY DATE
    // =========================================================

    @Override
    public List<ShowResponse> getShowsByDate(
            LocalDate date) {

        log.info(
                "Get shows by date request received. date={}",
                date
        );


        LocalDateTime start =
                date.atStartOfDay();

        LocalDateTime end =
                start.plusDays(1);


        List<Show> shows =
                showRepository.findByShowDateTimeBetween(
                        start,
                        end
                );


        log.info(
                "Shows fetched by date. date={}, count={}",
                date,
                shows.size()
        );


        shows.forEach(this::updateShowStatus);


        return shows.stream()
                .map(ShowTransformer::showToShowResponse)
                .toList();
    }


    // =========================================================
    // GET SHOWS BY THEATER
    // =========================================================

    @Override
    public List<ShowResponse> getShowsByTheater(
            Long theaterId) {

        log.info(
                "Get shows by theater request received. theaterId={}",
                theaterId
        );


        List<Show> shows =
                showRepository
                        .findByScreen_Theater_TheaterId(
                                theaterId
                        );


        log.info(
                "Shows fetched by theater. theaterId={}, count={}",
                theaterId,
                shows.size()
        );


        shows.forEach(this::updateShowStatus);


        return shows.stream()
                .map(ShowTransformer::showToShowResponse)
                .toList();
    }


    // =========================================================
    // GET SHOWS BY MOVIE AND DATE
    // =========================================================

    @Override
    public List<ShowResponse> getShowsByMovieAndDate(
            Long movieId,
            LocalDate date) {

        log.info(
                "Get shows by movie and date. movieId={}, date={}",
                movieId,
                date
        );


        movieRepository.findById(movieId)
                .orElseThrow(() -> {

                    log.warn(
                            "Movie not found. movieId={}",
                            movieId
                    );

                    return new MovieNotFoundException(
                            "Movie Not Found"
                    );
                });


        LocalDateTime start =
                date.atStartOfDay();

        LocalDateTime end =
                start.plusDays(1);


        List<Show> shows =
                showRepository
                        .findByMovie_MovieIdAndShowDateTimeBetween(
                                movieId,
                                start,
                                end
                        );


        log.info(
                "Shows fetched by movie and date. movieId={}, date={}, count={}",
                movieId,
                date,
                shows.size()
        );


        shows.forEach(this::updateShowStatus);


        return shows.stream()
                .map(ShowTransformer::showToShowResponse)
                .toList();
    }


    // =========================================================
    // GET SHOWS BY THEATER AND DATE
    // =========================================================

    @Override
    public List<ShowResponse> getShowsByTheaterAndDate(
            Long theaterId,
            LocalDate date) {

        log.info(
                "Get shows by theater and date. theaterId={}, date={}",
                theaterId,
                date
        );


        LocalDateTime start =
                date.atStartOfDay();

        LocalDateTime end =
                start.plusDays(1);


        List<Show> shows =
                showRepository
                        .findByScreen_Theater_TheaterIdAndShowDateTimeBetween(
                                theaterId,
                                start,
                                end
                        );


        log.info(
                "Shows fetched by theater and date. theaterId={}, count={}",
                theaterId,
                shows.size()
        );


        shows.forEach(this::updateShowStatus);


        return shows.stream()
                .map(ShowTransformer::showToShowResponse)
                .toList();
    }


    // =========================================================
    // GET SHOWS BY MOVIE, THEATER AND DATE
    // =========================================================

    @Override
    public List<ShowResponse> getShowsByMovieAndTheaterAndDate(
            Long movieId,
            Long theaterId,
            LocalDate date) {

        log.info(
                "Get shows by movie, theater and date. movieId={}, theaterId={}, date={}",
                movieId,
                theaterId,
                date
        );


        movieRepository.findById(movieId)
                .orElseThrow(() -> {

                    log.warn(
                            "Movie not found. movieId={}",
                            movieId
                    );

                    return new MovieNotFoundException(
                            "Movie Not Found"
                    );
                });


        LocalDateTime start =
                date.atStartOfDay();

        LocalDateTime end =
                start.plusDays(1);


        List<Show> shows =
                showRepository
                        .findByMovie_MovieIdAndScreen_Theater_TheaterIdAndShowDateTimeBetween(
                                movieId,
                                theaterId,
                                start,
                                end
                        );


        log.info(
                "Shows fetched by movie, theater and date. movieId={}, theaterId={}, date={}, count={}",
                movieId,
                theaterId,
                date,
                shows.size()
        );


        shows.forEach(this::updateShowStatus);


        return shows.stream()
                .map(ShowTransformer::showToShowResponse)
                .toList();
    }


    // =========================================================
    // UPDATE SHOW STATUS
    // =========================================================

    private void updateShowStatus(Show show) {

        if (
                show.getShowStatus() == ShowStatus.SCHEDULED
                        && show.getShowDateTime().isBefore(
                        LocalDateTime.now()
                )
        ) {

            show.setShowStatus(
                    ShowStatus.COMPLETED
            );

            showRepository.save(show);

            log.info(
                    "Show status updated to COMPLETED. showId={}",
                    show.getShowId()
            );
        }
    }
}