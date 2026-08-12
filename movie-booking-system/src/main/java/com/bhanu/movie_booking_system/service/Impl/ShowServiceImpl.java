package com.bhanu.movie_booking_system.service.Impl;

import com.bhanu.movie_booking_system.dto.request.ShowRequest;
import com.bhanu.movie_booking_system.dto.response.ShowResponse;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShowServiceImpl implements ShowService {
    @Autowired
    private ShowRepository showRepository;
    @Autowired
    private ScreenRepository screenRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private ShowSeatService showSeatService;
    @Override
    @Transactional
    public ShowResponse addShow(ShowRequest showRequest) {

        // Fetch Movie
        Movie movie = movieRepository.findById(showRequest.getMovieId())
                .orElseThrow(() ->
                        new MovieNotFoundException("Movie Not Found"));

        // Fetch Screen
        Screen screen = screenRepository.findById(showRequest.getScreenId())
                .orElseThrow(() ->
                        new ScreenNotFoundException("Screen Not Found"));

        // Convert DTO -> Entity
        Show show = ShowTransformer.showRequestToShow(showRequest);

        // Set Relationships
        show.setMovie(movie);
        show.setScreen(screen);

        // Save Show
        Show savedShow = showRepository.save(show);

        // Generate Show Seats
        showSeatService.generateShowSeats(savedShow, screen);

        // Return Response
        return ShowTransformer.showToShowResponse(savedShow);
    }

    @Override
    public List<ShowResponse> getAllShows() {

        List<Show> shows = showRepository.findAll();

        List<ShowResponse> responses = new ArrayList<>();

        for (Show show : shows) {
            responses.add(ShowTransformer.showToShowResponse(show));
        }

        return responses;
    }

    @Override
    public ShowResponse getShowById(Long showId) {

        Show show = showRepository.findById(showId)
                .orElseThrow(() ->
                        new ShowNotFoundException("Show Not Found"));

        return ShowTransformer.showToShowResponse(show);
    }

    @Override
    public ShowResponse updateShow(Long showId, ShowRequest showRequest) {

        Show show = showRepository.findById(showId)
                .orElseThrow(() ->
                        new ShowNotFoundException("Show Not Found"));

        Movie movie = movieRepository.findById(showRequest.getMovieId())
                .orElseThrow(() ->
                        new MovieNotFoundException("Movie Not Found"));

        Screen screen = screenRepository.findById(showRequest.getScreenId())
                .orElseThrow(() ->
                        new ScreenNotFoundException("Screen Not Found"));

        show.setMovie(movie);
        show.setScreen(screen);
        show.setShowDateTime(showRequest.getShowDateTime());
        show.setTicketPrice(showRequest.getTicketPrice());

        Show updatedShow = showRepository.save(show);

        return ShowTransformer.showToShowResponse(updatedShow);
    }

    @Override
    public void deleteShow(Long showId) {

        Show show = showRepository.findById(showId)
                .orElseThrow(() ->
                        new ShowNotFoundException("Show Not Found"));

        showRepository.delete(show);
    }
}
