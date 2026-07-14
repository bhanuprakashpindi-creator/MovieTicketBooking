package com.bhanu.movie_booking_system.repository;


import com.bhanu.movie_booking_system.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepository extends JpaRepository<Seat,Long> {
}
