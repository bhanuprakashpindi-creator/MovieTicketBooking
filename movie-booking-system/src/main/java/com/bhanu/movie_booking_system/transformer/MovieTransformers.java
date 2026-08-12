package com.bhanu.movie_booking_system.transformer;

import com.bhanu.movie_booking_system.dto.request.MovieRequest;
import com.bhanu.movie_booking_system.dto.response.MovieResponse;
import com.bhanu.movie_booking_system.entity.Movie;

public class MovieTransformers {
    public static Movie movieRequestToMovie(MovieRequest movieRequest)
    {
        Movie movie = new Movie();
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
        return movie;

    }
    public static MovieResponse movieToMovieResponse(Movie movie)
    {
        MovieResponse movieResponse=new MovieResponse();
        movieResponse.setMovieId(movie.getMovieId());
        movieResponse.setMovieTitle(movie.getMovieTitle());
        movieResponse.setGenre(movie.getGenre());
        movieResponse.setLanguage(movie.getLanguage());
        movieResponse.setDurationInMinutes(movie.getDurationInMinutes());
        movieResponse.setReleaseDate(movie.getReleaseDate());
        movieResponse.setRating(movie.getRating());
        movieResponse.setPosterUrl(movie.getPosterUrl());
        movieResponse.setDescription(movie.getDescription());
        movieResponse.setCertificate(movie.getCertificate());
        movieResponse.setDirector(movie.getDirector());
        movieResponse.setCast(movie.getCast());
        return movieResponse;
    }


}
