package com.bhanu.movie_booking_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bookedseats")
public class BookedSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookedSeatId;
    @ManyToOne
    @JoinColumn(name = "booking_id",nullable = false)
    private Booking booking;
    @ManyToOne
    @JoinColumn(name = "seat_id",nullable = false)
    private Seat seat;
}
