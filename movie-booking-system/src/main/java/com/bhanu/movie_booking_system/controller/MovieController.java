package com.bhanu.movie_booking_system.controller;

import com.bhanu.movie_booking_system.dto.request.MovieRequest;
import com.bhanu.movie_booking_system.dto.response.MovieResponse;
import com.bhanu.movie_booking_system.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @PostMapping
    public MovieResponse addMovie(@Valid @RequestBody MovieRequest movieRequest) {
        return movieService.addMovie(movieRequest);
    }

    @GetMapping
    public Page<MovieResponse> getAllMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return movieService.getMovies(page, size);
    }

    @GetMapping("/{movieId}")
    public MovieResponse getMovieById(@PathVariable Long movieId) {
        return movieService.getMovieById(movieId);
    }

    @PutMapping("/{movieId}")
    public MovieResponse updateMovie(@PathVariable Long movieId,
                                     @Valid @RequestBody MovieRequest movieRequest) {
        return movieService.updateMovie(movieId, movieRequest);
    }

    @DeleteMapping("/{movieId}")
    public String deleteMovie(@PathVariable Long movieId) {
        movieService.deleteMovie(movieId);
        return "Movie Deleted Successfully";
    }
    @GetMapping("/search")
    public List<MovieResponse> searchMovies(
            @RequestParam String title) {

        return movieService.searchMovies(title);
    }
    @GetMapping("/genre/{genre}")
    public List<MovieResponse> getMoviesByGenre(
            @PathVariable String genre) {

        return movieService.getMoviesByGenre(genre);
    }
    @GetMapping("/language/{language}")
    public List<MovieResponse> getMoviesByLanguage(
            @PathVariable String language) {

        return movieService.getMoviesByLanguage(language);
    }
    @GetMapping("/certificate/{certificate}")
    public List<MovieResponse> getMoviesByCertificate(
            @PathVariable String certificate) {

        return movieService.getMoviesByCertificate(certificate);
    }
    @GetMapping("/sort/rating")
    public List<MovieResponse> getMoviesSortedByRating() {

        return movieService.getMoviesSortedByRating();
    }
    @GetMapping("/rating")
    public List<MovieResponse> getMoviesByMinimumRating(
            @RequestParam Double rating) {

        return movieService.getMoviesByMinimumRating(rating);
    }
    @GetMapping("/sort/release-date")
    public List<MovieResponse> getMoviesSortedByReleaseDate() {

        return movieService.getMoviesSortedByReleaseDate();
    }


}