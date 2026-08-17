package com.bhanu.movie_booking_system.controller;

import com.bhanu.movie_booking_system.dto.request.ShowRequest;
import com.bhanu.movie_booking_system.dto.response.ShowResponse;
import com.bhanu.movie_booking_system.dto.response.ShowSeatResponse;
import com.bhanu.movie_booking_system.service.ShowSeatService;
import com.bhanu.movie_booking_system.service.ShowService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/shows")
public class ShowController {

    @Autowired
    private ShowService showService;
    @Autowired
    private ShowSeatService showSeatService;

    @PostMapping
    public ShowResponse addShow(@Valid @RequestBody ShowRequest showRequest) {
        return showService.addShow(showRequest);
    }

    @GetMapping
    public List<ShowResponse> getAllShows() {
        return showService.getAllShows();
    }

    @GetMapping("/{showId}")
    public ShowResponse getShowById(@PathVariable Long showId) {
        return showService.getShowById(showId);
    }

    @PutMapping("/{showId}")
    public ShowResponse updateShow(@PathVariable Long showId,
                                   @Valid @RequestBody ShowRequest showRequest) {
        return showService.updateShow(showId, showRequest);
    }

    @DeleteMapping("/{showId}")
    public String deleteShow(@PathVariable Long showId) {
        showService.deleteShow(showId);
        return "Show Deleted Successfully";
    }
    @GetMapping("/{showId}/seats")
    public List<ShowSeatResponse> getShowSeats(@PathVariable Long showId) {

        return showSeatService.getShowSeats(showId);
    }
    @GetMapping("/movie/{movieId}")
    public List<ShowResponse> getShowsByMovie(
            @PathVariable Long movieId) {

        return showService.getShowsByMovie(movieId);
    }

    @GetMapping("/date/{date}")
    public List<ShowResponse> getShowsByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        return showService.getShowsByDate(date);
    }
    @GetMapping("/theater/{theaterId}")
    public List<ShowResponse> getShowsByTheater(
            @PathVariable Long theaterId) {

        return showService.getShowsByTheater(theaterId);
    }
    @GetMapping("/movie/{movieId}/date/{date}")
    public List<ShowResponse> getShowsByMovieAndDate(
            @PathVariable Long movieId,
            @PathVariable LocalDate date) {

        return showService.getShowsByMovieAndDate(
                movieId,
                date
        );
    }
    @GetMapping("/theater/{theaterId}/date/{date}")
    public List<ShowResponse> getShowsByTheaterAndDate(
            @PathVariable Long theaterId,
            @PathVariable LocalDate date) {

        return showService.getShowsByTheaterAndDate(
                theaterId,
                date
        );
    }
    @GetMapping("/movie/{movieId}/theater/{theaterId}/date/{date}")
    public List<ShowResponse> getShowsByMovieAndTheaterAndDate(
            @PathVariable Long movieId,
            @PathVariable Long theaterId,
            @PathVariable LocalDate date) {

        return showService.getShowsByMovieAndTheaterAndDate(
                movieId,
                theaterId,
                date
        );
    }

}