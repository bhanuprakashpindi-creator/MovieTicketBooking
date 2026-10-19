package com.bhanu.movie_booking_system.controller;

import com.bhanu.movie_booking_system.dto.request.SeatRequest;
import com.bhanu.movie_booking_system.dto.response.SeatResponse;
import com.bhanu.movie_booking_system.service.SeatService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
public class SeatController {

    @Autowired
    private SeatService seatService;

    @PostMapping
    public SeatResponse addSeat(
            @Valid @RequestBody SeatRequest seatRequest) {

        return seatService.addSeat(seatRequest);
    }

    @GetMapping
    public List<SeatResponse> getAllSeats() {

        return seatService.getAllSeats();
    }

    @GetMapping("/{seatId}")
    public SeatResponse getSeatById(
            @PathVariable Long seatId) {

        return seatService.getSeatById(seatId);
    }

    @PutMapping("/{seatId}")
    public SeatResponse updateSeat(
            @PathVariable Long seatId,
            @Valid @RequestBody SeatRequest seatRequest) {

        return seatService.updateSeat(seatId, seatRequest);
    }

    @DeleteMapping("/{seatId}")
    public String deleteSeat(
            @PathVariable Long seatId) {

        seatService.deleteSeat(seatId);

        return "Seat Deleted Successfully";
    }
    @GetMapping("/screen/{screenId}")
    public List<SeatResponse> getSeatsByScreen(@PathVariable Long screenId) {
        return seatService.getSeatsByScreen(screenId);
    }
}