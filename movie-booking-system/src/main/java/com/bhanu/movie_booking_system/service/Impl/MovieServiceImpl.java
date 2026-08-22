package com.bhanu.movie_booking_system.service.Impl;

import com.bhanu.movie_booking_system.entity.Enum.Certificate;
import com.bhanu.movie_booking_system.entity.Enum.Genre;
import com.bhanu.movie_booking_system.entity.Enum.Language;
import com.bhanu.movie_booking_system.transformer.MovieTransformers;
import com.bhanu.movie_booking_system.dto.request.MovieRequest;
import com.bhanu.movie_booking_system.dto.response.MovieResponse;
import com.bhanu.movie_booking_system.entity.Movie;
import com.bhanu.movie_booking_system.exception.MovieAlreadyExistsException;
import com.bhanu.movie_booking_system.exception.MovieNotFoundException;
import com.bhanu.movie_booking_system.repository.MovieRepository;
import com.bhanu.movie_booking_system.service.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    private static final Logger log =
            LoggerFactory.getLogger(MovieServiceImpl.class);

    @Autowired
    private MovieRepository movieRepository;


    // =========================================================
    // ADD MOVIE
    // =========================================================

    @Override
    public MovieResponse addMovie(MovieRequest movieRequest) {

        log.info(
                "Movie creation request received. title={}",
                movieRequest.getMovieTitle()
        );

        Movie movie =
                MovieTransformers.movieRequestToMovie(movieRequest);


        if (movieRepository
                .findByMovieTitle(movieRequest.getMovieTitle())
                .isPresent()) {

            log.warn(
                    "Movie creation failed. Movie already exists. title={}",
                    movieRequest.getMovieTitle()
            );

            throw new MovieAlreadyExistsException(
                    "Movie Already Exists"
            );
        }


        Movie savedMovie =
                movieRepository.save(movie);


        log.info(
                "Movie created successfully. movieId={}, title={}",
                savedMovie.getMovieId(),
                savedMovie.getMovieTitle()
        );


        return MovieTransformers.movieToMovieResponse(
                savedMovie
        );
    }


    // =========================================================
    // GET ALL MOVIES
    // =========================================================

    @Override
    public List<MovieResponse> getAllMovies() {

        log.info("Get all movies request received");

        List<MovieResponse> movies =
                new ArrayList<>();

        List<Movie> allMovies =
                movieRepository.findAll();


        log.debug(
                "Movies retrieved from database. count={}",
                allMovies.size()
        );


        for (Movie movie : allMovies) {

            movies.add(
                    MovieTransformers.movieToMovieResponse(movie)
            );
        }


        log.info(
                "All movies fetched successfully. count={}",
                movies.size()
        );


        return movies;
    }


    // =========================================================
    // GET MOVIE BY ID
    // =========================================================

    @Override
    public MovieResponse getMovieById(Long movieId) {

        log.info(
                "Get movie request received. movieId={}",
                movieId
        );


        Movie movie =
                movieRepository.findById(movieId)
                        .orElseThrow(() -> {

                            log.warn(
                                    "Movie not found. movieId={}",
                                    movieId
                            );

                            return new MovieNotFoundException(
                                    "Movie Not Found!"
                            );
                        });


        log.info(
                "Movie fetched successfully. movieId={}, title={}",
                movieId,
                movie.getMovieTitle()
        );


        return MovieTransformers.movieToMovieResponse(
                movie
        );
    }


    // =========================================================
    // UPDATE MOVIE
    // =========================================================

    @Override
    public MovieResponse updateMovie(
            Long movieId,
            MovieRequest movieRequest) {

        log.info(
                "Movie update request received. movieId={}",
                movieId
        );


        Movie movie =
                movieRepository.findById(movieId)
                        .orElseThrow(() -> {

                            log.warn(
                                    "Movie update failed. Movie not found. movieId={}",
                                    movieId
                            );

                            return new MovieNotFoundException(
                                    "Movie Not Found!"
                            );
                        });


        movie.setMovieTitle(
                movieRequest.getMovieTitle()
        );

        movie.setGenre(
                movieRequest.getGenre()
        );

        movie.setLanguage(
                movieRequest.getLanguage()
        );

        movie.setDurationInMinutes(
                movieRequest.getDurationInMinutes()
        );

        movie.setReleaseDate(
                movieRequest.getReleaseDate()
        );

        movie.setRating(
                movieRequest.getRating()
        );

        movie.setPosterUrl(
                movieRequest.getPosterUrl()
        );

        movie.setDescription(
                movieRequest.getDescription()
        );

        movie.setCertificate(
                movieRequest.getCertificate()
        );

        movie.setDirector(
                movieRequest.getDirector()
        );

        movie.setCast(
                movieRequest.getCast()
        );

        movie.setTrailerUrl(
                movieRequest.getTrailerUrl()
        );


        Movie updatedMovie =
                movieRepository.save(movie);


        log.info(
                "Movie updated successfully. movieId={}, title={}",
                updatedMovie.getMovieId(),
                updatedMovie.getMovieTitle()
        );


        return MovieTransformers.movieToMovieResponse(
                updatedMovie
        );
    }


    // =========================================================
    // DELETE MOVIE
    // =========================================================

    @Override
    public void deleteMovie(Long movieId) {

        log.info(
                "Movie deletion request received. movieId={}",
                movieId
        );


        Movie movie =
                movieRepository.findById(movieId)
                        .orElseThrow(() -> {

                            log.warn(
                                    "Movie deletion failed. Movie not found. movieId={}",
                                    movieId
                            );

                            return new MovieNotFoundException(
                                    "Movie Not Found!"
                            );
                        });


        movieRepository.delete(movie);


        log.info(
                "Movie deleted successfully. movieId={}, title={}",
                movieId,
                movie.getMovieTitle()
        );
    }


    // =========================================================
    // SEARCH MOVIES
    // =========================================================

    @Override
    public List<MovieResponse> searchMovies(String title) {

        log.info(
                "Movie search request received. title={}",
                title
        );


        List<Movie> movies =
                movieRepository
                        .findByMovieTitleContainingIgnoreCase(title);


        log.debug(
                "Movie search completed. title={}, results={}",
                title,
                movies.size()
        );


        return movies.stream()
                .map(MovieTransformers::movieToMovieResponse)
                .toList();
    }


    // =========================================================
    // GET MOVIES BY GENRE
    // =========================================================

    @Override
    public List<MovieResponse> getMoviesByGenre(
            String genre) {

        log.info(
                "Movie genre filter request received. genre={}",
                genre
        );


        Genre genreEnum =
                Genre.valueOf(
                        genre.toUpperCase()
                );


        List<Movie> movies =
                movieRepository.findByGenre(
                        genreEnum
                );


        log.debug(
                "Movies filtered by genre. genre={}, results={}",
                genre,
                movies.size()
        );


        return movies.stream()
                .map(MovieTransformers::movieToMovieResponse)
                .toList();
    }


    // =========================================================
    // GET MOVIES BY LANGUAGE
    // =========================================================

    @Override
    public List<MovieResponse> getMoviesByLanguage(
            String language) {

        log.info(
                "Movie language filter request received. language={}",
                language
        );


        Language languageEnum =
                Language.valueOf(
                        language.toUpperCase()
                );


        List<Movie> movies =
                movieRepository.findByLanguage(
                        languageEnum
                );


        log.debug(
                "Movies filtered by language. language={}, results={}",
                language,
                movies.size()
        );


        return movies.stream()
                .map(MovieTransformers::movieToMovieResponse)
                .toList();
    }


    // =========================================================
    // GET MOVIES BY CERTIFICATE
    // =========================================================

    @Override
    public List<MovieResponse> getMoviesByCertificate(
            String certificate) {

        log.info(
                "Movie certificate filter request received. certificate={}",
                certificate
        );


        Certificate certificateEnum =
                Certificate.valueOf(
                        certificate.toUpperCase()
                );


        List<Movie> movies =
                movieRepository.findByCertificate(
                        certificateEnum
                );


        log.debug(
                "Movies filtered by certificate. certificate={}, results={}",
                certificate,
                movies.size()
        );


        return movies.stream()
                .map(MovieTransformers::movieToMovieResponse)
                .toList();
    }


    // =========================================================
    // SORT MOVIES BY RATING
    // =========================================================

    @Override
    public List<MovieResponse> getMoviesSortedByRating() {

        log.info(
                "Get movies sorted by rating request received"
        );


        List<Movie> movies =
                movieRepository
                        .findAllByOrderByRatingDesc();


        log.debug(
                "Movies sorted by rating. results={}",
                movies.size()
        );


        return movies.stream()
                .map(MovieTransformers::movieToMovieResponse)
                .toList();
    }


    // =========================================================
    // PAGINATION
    // =========================================================

    @Override
    public Page<MovieResponse> getMovies(
            int page,
            int size) {

        log.info(
                "Movie pagination request received. page={}, size={}",
                page,
                size
        );


        Pageable pageable =
                PageRequest.of(page, size);


        Page<Movie> movies =
                movieRepository.findAll(pageable);


        log.debug(
                "Movie pagination completed. page={}, size={}, totalElements={}",
                page,
                size,
                movies.getTotalElements()
        );


        return movies.map(
                MovieTransformers::movieToMovieResponse
        );
    }


    // =========================================================
    // MINIMUM RATING
    // =========================================================

    @Override
    public List<MovieResponse> getMoviesByMinimumRating(
            Double rating) {

        log.info(
                "Minimum rating filter request received. rating={}",
                rating
        );


        List<Movie> movies =
                movieRepository
                        .findByRatingGreaterThanEqual(rating);


        log.debug(
                "Movies filtered by minimum rating. rating={}, results={}",
                rating,
                movies.size()
        );


        return movies.stream()
                .map(MovieTransformers::movieToMovieResponse)
                .toList();
    }


    // =========================================================
    // SORT BY RELEASE DATE
    // =========================================================

    @Override
    public List<MovieResponse> getMoviesSortedByReleaseDate() {

        log.info(
                "Get movies sorted by release date request received"
        );


        List<Movie> movies =
                movieRepository
                        .findAllByOrderByReleaseDateDesc();


        log.debug(
                "Movies sorted by release date. results={}",
                movies.size()
        );


        return movies.stream()
                .map(MovieTransformers::movieToMovieResponse)
                .toList();
    }
}