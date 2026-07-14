package com.bhanu.movie_booking_system.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private LocalDateTime timeStamp;
    private int status;
    private String message;
    private String path;
}
