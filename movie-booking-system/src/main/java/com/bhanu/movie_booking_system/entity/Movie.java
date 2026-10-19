package com.bhanu.movie_booking_system.entity;


import com.bhanu.movie_booking_system.entity.Enum.Certificate;
import com.bhanu.movie_booking_system.entity.Enum.Genre;
import com.bhanu.movie_booking_system.entity.Enum.Language;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movieId;
    @Column(nullable = false)
    private String movieTitle;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Genre genre;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Language language;
    @Column(nullable = false)
    private long durationInMinutes;
    @Column(nullable = false)
    private LocalDate releaseDate;
    @Column(nullable = false)
    private double rating;
    @Column(nullable = false)
    private String posterUrl;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Certificate certificate;
    @Column(nullable = false)
    private String director;

    @Column(nullable = false)
    private String cast;
    @Column(nullable = false)
    private String trailerUrl;
    @Column(nullable = false)
    private boolean isActive = true;

    @OneToMany(mappedBy = "movie")
    private List<Show> shows;
}
