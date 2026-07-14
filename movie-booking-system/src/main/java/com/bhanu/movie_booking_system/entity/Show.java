package com.bhanu.movie_booking_system.entity;

import com.bhanu.movie_booking_system.entity.Enum.ShowStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shows")
public class Show {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long showId;
    @Column(nullable = false)
    private LocalDate showDate;
    @Column(nullable = false)
    private LocalTime showTime;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShowStatus showStatus;
    @ManyToOne
    @JoinColumn(name = "movie_id",nullable = false)
    private Movie movie;
    @ManyToOne
    @JoinColumn(name = "screen_id",nullable = false)
    private Screen screen;
    @OneToMany(mappedBy = "show")
    private List<Booking> bookings;
}
