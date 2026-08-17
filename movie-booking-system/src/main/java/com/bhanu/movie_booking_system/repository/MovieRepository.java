package com.bhanu.movie_booking_system.repository;

import com.bhanu.movie_booking_system.entity.Enum.Certificate;
import com.bhanu.movie_booking_system.entity.Enum.Genre;
import com.bhanu.movie_booking_system.entity.Enum.Language;
import com.bhanu.movie_booking_system.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    Optional<Movie> findByMovieTitle(String movieTitle);

    List<Movie> findByMovieTitleContainingIgnoreCase(String movieTitle);

    List<Movie> findByGenre(Genre genre);

    List<Movie> findByLanguage(Language language);

    List<Movie> findByCertificate(Certificate certificate);

    List<Movie> findAllByOrderByRatingDesc();
    List<Movie> findByRatingGreaterThanEqual(Double rating);
    List<Movie> findAllByOrderByReleaseDateDesc();

}