package com.bhanu.movie_booking_system.repository;

import com.bhanu.movie_booking_system.entity.Booking;
import com.bhanu.movie_booking_system.entity.Enum.BookingStatus;
import com.bhanu.movie_booking_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {
    List<Booking> findByBookingStatus(BookingStatus bookingStatus);

    List<Booking> findByUser(User user);

    List<Booking> findByUserAndBookingStatus(
            User user,
            BookingStatus bookingStatus
    );
}
