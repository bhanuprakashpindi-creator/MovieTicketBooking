package com.bhanu.movie_booking_system.dto.response;

import com.bhanu.movie_booking_system.entity.Enum.Certificate;
import com.bhanu.movie_booking_system.entity.Enum.Genre;
import com.bhanu.movie_booking_system.entity.Enum.Language;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieResponse {

    private Long movieId;
    private String movieTitle;
    private Genre genre;
    private Language language;
    private Long durationInMinutes;
    private LocalDate releaseDate;
    private Double rating;
    private String posterUrl;
    private String description;
    private Certificate certificate;
    private String director;
    private String cast;
    private String trailerUrl;
}