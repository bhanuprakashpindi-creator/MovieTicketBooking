package com.bhanu.movie_booking_system.repository;

import com.bhanu.movie_booking_system.entity.ShowSeat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShowSeatRepository extends JpaRepository<ShowSeat, Long> {

    List<ShowSeat> findByShow_ShowId(Long showId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT ss
            FROM ShowSeat ss
            WHERE ss.show.showId = :showId
            AND ss.showSeatId IN :seatIds
            """)
    List<ShowSeat> findSeatsForBooking(
            @Param("showId") Long showId,
            @Param("seatIds") List<Long> seatIds
    );

}