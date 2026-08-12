package com.bhanu.movie_booking_system.repository;


import com.bhanu.movie_booking_system.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie,Long> {
    Optional<Movie> findByMovieTitle(String movieTitle);
}
