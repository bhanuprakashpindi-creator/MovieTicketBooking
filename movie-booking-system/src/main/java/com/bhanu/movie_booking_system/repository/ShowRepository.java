package com.bhanu.movie_booking_system.repository;

import com.bhanu.movie_booking_system.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show,Long> {
    List<Show> findByMovie_MovieId(Long movieId);
    List<Show> findByShowDateTimeBetween(
            LocalDateTime start,
            LocalDateTime end
    );
    List<Show> findByScreen_Theater_TheaterId(Long theaterId);
    List<Show> findByMovie_MovieIdAndShowDateTimeBetween(
            Long movieId,
            LocalDateTime start,
            LocalDateTime end
    );

    List<Show> findByScreen_Theater_TheaterIdAndShowDateTimeBetween(
            Long theaterId,
            LocalDateTime start,
            LocalDateTime end
    );

    List<Show> findByMovie_MovieIdAndScreen_Theater_TheaterIdAndShowDateTimeBetween(
            Long movieId,
            Long theaterId,
            LocalDateTime start,
            LocalDateTime end
    );
}
