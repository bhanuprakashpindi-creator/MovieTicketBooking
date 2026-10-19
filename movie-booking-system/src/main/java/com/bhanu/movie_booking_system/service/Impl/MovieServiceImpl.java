package com.bhanu.movie_booking_system.service.Impl;

import com.bhanu.movie_booking_system.dto.request.MovieRequest;
import com.bhanu.movie_booking_system.dto.response.MovieResponse;
import com.bhanu.movie_booking_system.entity.Enum.Certificate;
import com.bhanu.movie_booking_system.entity.Enum.Genre;
import com.bhanu.movie_booking_system.entity.Enum.Language;
import com.bhanu.movie_booking_system.entity.Enum.ShowStatus;
import com.bhanu.movie_booking_system.entity.Movie;
import com.bhanu.movie_booking_system.exception.MovieAlreadyExistsException;
import com.bhanu.movie_booking_system.exception.MovieNotFoundException;
import com.bhanu.movie_booking_system.repository.MovieRepository;
import com.bhanu.movie_booking_system.repository.ShowRepository;
import com.bhanu.movie_booking_system.service.MovieService;
import com.bhanu.movie_booking_system.transformer.MovieTransformers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    private static final Logger log =
            LoggerFactory.getLogger(MovieServiceImpl.class);

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ShowRepository showRepository;


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

        // Newly created movies are active by default.
        movie.setActive(true);

        Movie savedMovie =
                movieRepository.save(movie);

        log.info(
                "Movie created successfully. movieId={}, title={}",
                savedMovie.getMovieId(),
                savedMovie.getMovieTitle()
        );

        return MovieTransformers.movieToMovieResponse(savedMovie);
    }


    // =========================================================
    // GET ALL ACTIVE MOVIES
    // =========================================================

    @Override
    public List<MovieResponse> getAllMovies() {

        log.info("Get all active movies request received");

        List<Movie> activeMovies =
                movieRepository.findByIsActiveTrue();

        List<MovieResponse> movies =
                activeMovies.stream()
                        .map(MovieTransformers::movieToMovieResponse)
                        .toList();

        log.info(
                "Active movies fetched successfully. count={}",
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

        return MovieTransformers.movieToMovieResponse(movie);
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

        movie.setMovieTitle(movieRequest.getMovieTitle());
        movie.setGenre(movieRequest.getGenre());
        movie.setLanguage(movieRequest.getLanguage());

        movie.setDurationInMinutes(
                movieRequest.getDurationInMinutes()
        );

        movie.setReleaseDate(movieRequest.getReleaseDate());
        movie.setRating(movieRequest.getRating());
        movie.setPosterUrl(movieRequest.getPosterUrl());
        movie.setDescription(movieRequest.getDescription());
        movie.setCertificate(movieRequest.getCertificate());
        movie.setDirector(movieRequest.getDirector());
        movie.setCast(movieRequest.getCast());
        movie.setTrailerUrl(movieRequest.getTrailerUrl());

        Movie updatedMovie =
                movieRepository.save(movie);

        log.info(
                "Movie updated successfully. movieId={}, title={}",
                updatedMovie.getMovieId(),
                updatedMovie.getMovieTitle()
        );

        return MovieTransformers.movieToMovieResponse(updatedMovie);
    }


    // =========================================================
    // DELETE / ARCHIVE MOVIE
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

        // Prevent archiving movies with scheduled or running shows.
        boolean hasActiveShows =
                showRepository.existsByMovie_MovieIdAndShowStatusIn(
                        movieId,
                        List.of(
                                ShowStatus.SCHEDULED,
                                ShowStatus.RUNNING
                        )
                );

        if (hasActiveShows) {

            log.warn(
                    "Movie archiving rejected because active shows exist. movieId={}",
                    movieId
            );

            throw new IllegalStateException(
                    "Cannot archive this movie because it has scheduled or running shows."
            );
        }

        // Check whether historical shows reference this movie.
        boolean hasAssociatedShows =
                showRepository.existsByMovie_MovieId(movieId);

        if (hasAssociatedShows) {

            if (!movie.isActive()) {

                log.info(
                        "Movie is already archived. movieId={}, title={}",
                        movieId,
                        movie.getMovieTitle()
                );

                return;
            }

            // Preserve historical shows and bookings.
            movie.setActive(false);

            movieRepository.save(movie);

            log.info(
                    "Movie archived successfully. movieId={}, title={}",
                    movieId,
                    movie.getMovieTitle()
            );

            return;
        }

        // No shows reference this movie, so permanent deletion is allowed.
        movieRepository.delete(movie);

        log.info(
                "Movie permanently deleted successfully. movieId={}, title={}",
                movieId,
                movie.getMovieTitle()
        );
    }


    // =========================================================
    // SEARCH ACTIVE MOVIES
    // =========================================================

    @Override
    public List<MovieResponse> searchMovies(String title) {

        log.info(
                "Movie search request received. title={}",
                title
        );

        List<Movie> movies =
                movieRepository
                        .findByMovieTitleContainingIgnoreCaseAndIsActiveTrue(
                                title
                        );

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
    // GET ACTIVE MOVIES BY GENRE
    // =========================================================

    @Override
    public List<MovieResponse> getMoviesByGenre(String genre) {

        log.info(
                "Movie genre filter request received. genre={}",
                genre
        );

        Genre genreEnum =
                Genre.valueOf(genre.toUpperCase());

        List<Movie> movies =
                movieRepository.findByGenreAndIsActiveTrue(
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
    // GET ACTIVE MOVIES BY LANGUAGE
    // =========================================================

    @Override
    public List<MovieResponse> getMoviesByLanguage(String language) {

        log.info(
                "Movie language filter request received. language={}",
                language
        );

        Language languageEnum =
                Language.valueOf(language.toUpperCase());

        List<Movie> movies =
                movieRepository.findByLanguageAndIsActiveTrue(
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
    // GET ACTIVE MOVIES BY CERTIFICATE
    // =========================================================

    @Override
    public List<MovieResponse> getMoviesByCertificate(
            String certificate) {

        log.info(
                "Movie certificate filter request received. certificate={}",
                certificate
        );

        Certificate certificateEnum =
                Certificate.valueOf(certificate.toUpperCase());

        List<Movie> movies =
                movieRepository.findByCertificateAndIsActiveTrue(
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
    // SORT ACTIVE MOVIES BY RATING
    // =========================================================

    @Override
    public List<MovieResponse> getMoviesSortedByRating() {

        log.info(
                "Get active movies sorted by rating request received"
        );

        List<Movie> movies =
                movieRepository
                        .findByIsActiveTrueOrderByRatingDesc();

        log.debug(
                "Movies sorted by rating. results={}",
                movies.size()
        );

        return movies.stream()
                .map(MovieTransformers::movieToMovieResponse)
                .toList();
    }


    // =========================================================
    // PAGINATION - ACTIVE MOVIES ONLY
    // =========================================================

    @Override
    public Page<MovieResponse> getMovies(int page, int size) {

        log.info(
                "Movie pagination request received. page={}, size={}",
                page,
                size
        );

        Pageable pageable =
                PageRequest.of(page, size);

        Page<Movie> movies =
                movieRepository.findByIsActiveTrue(pageable);

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
    // GET ACTIVE MOVIES BY MINIMUM RATING
    // =========================================================

    @Override
    public List<MovieResponse> getMoviesByMinimumRating(Double rating) {

        log.info(
                "Minimum rating filter request received. rating={}",
                rating
        );

        List<Movie> movies =
                movieRepository
                        .findByRatingGreaterThanEqualAndIsActiveTrue(
                                rating
                        );

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
    // SORT ACTIVE MOVIES BY RELEASE DATE
    // =========================================================

    @Override
    public List<MovieResponse> getMoviesSortedByReleaseDate() {

        log.info(
                "Get active movies sorted by release date request received"
        );

        List<Movie> movies =
                movieRepository
                        .findByIsActiveTrueOrderByReleaseDateDesc();

        log.debug(
                "Movies sorted by release date. results={}",
                movies.size()
        );

        return movies.stream()
                .map(MovieTransformers::movieToMovieResponse)
                .toList();
    }
}