package com.bhanu.movie_booking_system.dto.request;


import com.bhanu.movie_booking_system.entity.Enum.Certificate;
import com.bhanu.movie_booking_system.entity.Enum.Genre;
import com.bhanu.movie_booking_system.entity.Enum.Language;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
    public class MovieRequest {

        @NotBlank(message = "Title is required")
        private String movieTitle;

        @NotNull(message = "Genre is required")
        private Genre genre;

        @NotNull(message = "Language is required")
        private Language language;

        @NotNull(message = "Duration is required")
        private Long durationInMinutes;

        @NotNull(message = "Release date is required")
        private LocalDate releaseDate;

        @NotNull(message = "Rating is required")
        private Double rating;

        @NotBlank(message = "Poster URL is required")
        private String posterUrl;

        @NotBlank(message = "Description is required")
        private String description;

        @NotNull(message = "Certificate is required")
        private Certificate certificate;

        @NotBlank(message = "Director is required")
        private String director;

        @NotBlank(message = "Cast is required")
        private String cast;

        @NotBlank(message = "Trailer URL is required")
        private String trailerUrl;
    }

