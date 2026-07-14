package com.bhanu.movie_booking_system.repository;

import com.bhanu.movie_booking_system.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowRepository extends JpaRepository<Show,Long> {
}
