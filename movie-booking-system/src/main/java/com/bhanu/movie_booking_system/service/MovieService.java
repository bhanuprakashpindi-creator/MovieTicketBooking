package com.bhanu.movie_booking_system.service;

import com.bhanu.movie_booking_system.dto.request.MovieRequest;
import com.bhanu.movie_booking_system.dto.response.MovieResponse;

import java.util.List;

public interface MovieService {
    MovieResponse addMovie(MovieRequest movieRequest);

    List<MovieResponse> getAllMovies();

    MovieResponse getMovieById(Long movieId);

    MovieResponse updateMovie(Long movieId, MovieRequest movieRequest);

    void deleteMovie(Long movieId);
}
