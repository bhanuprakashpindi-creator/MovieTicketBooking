package com.bhanu.movie_booking_system.service.Impl;

import com.bhanu.movie_booking_system.transformer.MovieTransformers;
import com.bhanu.movie_booking_system.dto.request.MovieRequest;
import com.bhanu.movie_booking_system.dto.response.MovieResponse;
import com.bhanu.movie_booking_system.entity.Movie;
import com.bhanu.movie_booking_system.exception.MovieAlreadyExistsException;
import com.bhanu.movie_booking_system.exception.MovieNotFoundException;
import com.bhanu.movie_booking_system.repository.MovieRepository;
import com.bhanu.movie_booking_system.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class MovieServiceImpl implements MovieService {
    @Autowired
    private MovieRepository movieRepository;
    @Override
    public MovieResponse addMovie(MovieRequest movieRequest) {
        Movie movie=MovieTransformers.movieRequestToMovie(movieRequest);
        if (movieRepository.findByMovieTitle(movieRequest.getMovieTitle()).isPresent()) {
            throw new MovieAlreadyExistsException("Movie Already Exists");
        }
        Movie savedMovie = movieRepository.save(movie);
        return MovieTransformers.movieToMovieResponse(savedMovie);
    }

    @Override
    public List<MovieResponse> getAllMovies() {
        List<MovieResponse> movies=new ArrayList<>();
        List<Movie> allmovies = movieRepository.findAll();
        for(Movie movie : allmovies )
        {
            movies.add(MovieTransformers.movieToMovieResponse(movie));
        }
    return movies;
    }

    @Override
    public MovieResponse getMovieById(Long movieId) {
        return MovieTransformers.movieToMovieResponse(movieRepository.findById(movieId).orElseThrow(() -> new RuntimeException("Movie Not Found!")));
    }

    @Override
    public MovieResponse updateMovie(Long movieId, MovieRequest movieRequest) {

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Movie Not Found!"));

        movie.setMovieTitle(movieRequest.getMovieTitle());
        movie.setGenre(movieRequest.getGenre());
        movie.setLanguage(movieRequest.getLanguage());
        movie.setDurationInMinutes(movieRequest.getDurationInMinutes());
        movie.setReleaseDate(movieRequest.getReleaseDate());
        movie.setRating(movieRequest.getRating());
        movie.setPosterUrl(movieRequest.getPosterUrl());
        movie.setDescription(movieRequest.getDescription());
        movie.setCertificate(movieRequest.getCertificate());
        movie.setDirector(movieRequest.getDirector());
        movie.setCast(movieRequest.getCast());
        movie.setTrailerUrl(movieRequest.getTrailerUrl());


        Movie updatedMovie = movieRepository.save(movie);


        return MovieTransformers.movieToMovieResponse(updatedMovie);
    }

    @Override
    public void deleteMovie(Long movieId) {

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Movie Not Found!"));

        movieRepository.delete(movie);
    }
}
