package com.bhanu.movie_booking_system.repository;

import com.bhanu.movie_booking_system.entity.ShowSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShowSeatRepository extends JpaRepository<ShowSeat, Long> {

    List<ShowSeat> findByShow_ShowId(Long showId);

}