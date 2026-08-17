package com.bhanu.movie_booking_system.service;

import com.bhanu.movie_booking_system.dto.request.MovieRequest;
import com.bhanu.movie_booking_system.dto.response.MovieResponse;
import com.bhanu.movie_booking_system.entity.Movie;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface MovieService {
    MovieResponse addMovie(MovieRequest movieRequest);

    List<MovieResponse> getAllMovies();

    MovieResponse getMovieById(Long movieId);

    MovieResponse updateMovie(Long movieId, MovieRequest movieRequest);

    void deleteMovie(Long movieId);

    List<MovieResponse> searchMovies(String title);

    List<MovieResponse> getMoviesByGenre(String genre);

    List<MovieResponse> getMoviesByLanguage(String language);

    List<MovieResponse> getMoviesByCertificate(String certificate);

    List<MovieResponse> getMoviesSortedByRating();

    Page<MovieResponse> getMovies(int page, int size);

    List<MovieResponse> getMoviesByMinimumRating(Double rating);
    List<MovieResponse> getMoviesSortedByReleaseDate();
}
