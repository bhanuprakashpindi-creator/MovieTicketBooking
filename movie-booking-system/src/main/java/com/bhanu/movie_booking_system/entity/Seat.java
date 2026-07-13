package com.bhanu.movie_booking_system.entity;

import com.bhanu.movie_booking_system.entity.Enum.SeatType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seatId;
    @Column(nullable = false)
    private String seatNumber;
    @Column(nullable = false)
    private String rowName;
    @Column(nullable = false)
    private Integer seatNumberInRow;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SeatType seatType;
    @Column(nullable = false)
    private double price;
    @ManyToOne
    @JoinColumn(name = "screen_id",nullable = false)
    private Screen screen;
}
