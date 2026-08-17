package com.bhanu.movie_booking_system.entity;

import com.bhanu.movie_booking_system.entity.Enum.SeatStatus;
import com.bhanu.movie_booking_system.entity.Seat;
import com.bhanu.movie_booking_system.entity.Show;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "show_seats")
public class ShowSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long showSeatId;

    @ManyToOne
    @JoinColumn(name = "show_id", nullable = false)
    private Show show;

    @ManyToOne
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatStatus seatStatus;

    @Version
    @Column(nullable = false)
    private Long version = 0L;
}