package com.bhanu.movie_booking_system.entity;

import com.bhanu.movie_booking_system.entity.Enum.ScreenType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Screen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long screenId;

    @Column(nullable = false)
    private String screenName;
    @Column(nullable = false)

    @Enumerated(EnumType.STRING)
    private ScreenType screenType;
    @Column(nullable = false)

    private Integer totalSeats;
    @Column(nullable = false)

    @JoinColumn(name = "theater_id",nullable = false)
    @ManyToOne
    private Theater theater;
    @OneToMany(mappedBy = "screen")
    private List<Show> shows;

    @OneToMany(mappedBy = "screen")
    private List<Seat> seats;

}
