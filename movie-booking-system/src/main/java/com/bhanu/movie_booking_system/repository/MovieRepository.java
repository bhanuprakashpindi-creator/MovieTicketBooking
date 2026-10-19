package com.bhanu.movie_booking_system.repository;

import com.bhanu.movie_booking_system.entity.Enum.Certificate;
import com.bhanu.movie_booking_system.entity.Enum.Genre;
import com.bhanu.movie_booking_system.entity.Enum.Language;
import com.bhanu.movie_booking_system.entity.Movie;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    // =========================================================
    // EXISTING METHODS
    // =========================================================

    Optional<Movie> findByMovieTitle(String movieTitle);

    List<Movie> findByMovieTitleContainingIgnoreCase(String movieTitle);

    List<Movie> findByGenre(Genre genre);

    List<Movie> findByLanguage(Language language);

    List<Movie> findByCertificate(Certificate certificate);

    List<Movie> findAllByOrderByRatingDesc();

    List<Movie> findByRatingGreaterThanEqual(Double rating);

    List<Movie> findAllByOrderByReleaseDateDesc();


    // =========================================================
    // ACTIVE MOVIE METHODS
    // =========================================================

    // Get all active movies
    List<Movie> findByIsActiveTrue();

    // Search active movies by title
    List<Movie> findByMovieTitleContainingIgnoreCaseAndIsActiveTrue(
            String movieTitle
    );

    // Filter active movies by genre
    List<Movie> findByGenreAndIsActiveTrue(Genre genre);

    // Filter active movies by language
    List<Movie> findByLanguageAndIsActiveTrue(Language language);

    // Filter active movies by certificate
    List<Movie> findByCertificateAndIsActiveTrue(
            Certificate certificate
    );

    // Sort active movies by rating
    List<Movie> findByIsActiveTrueOrderByRatingDesc();

    // Filter active movies by minimum rating
    List<Movie> findByRatingGreaterThanEqualAndIsActiveTrue(
            Double rating
    );

    // Sort active movies by release date
    List<Movie> findByIsActiveTrueOrderByReleaseDateDesc();

    // Paginate active movies
    Page<Movie> findByIsActiveTrue(Pageable pageable);
}