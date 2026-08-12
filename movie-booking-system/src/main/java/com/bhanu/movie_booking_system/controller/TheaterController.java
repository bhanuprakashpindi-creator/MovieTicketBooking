package com.bhanu.movie_booking_system.controller;

import com.bhanu.movie_booking_system.dto.request.TheaterRequest;
import com.bhanu.movie_booking_system.dto.response.TheaterResponse;
import com.bhanu.movie_booking_system.service.TheaterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/theaters")
public class TheaterController {

    @Autowired
    private TheaterService theaterService;

    @PostMapping
    public TheaterResponse addTheater(@Valid @RequestBody TheaterRequest theaterRequest) {
        return theaterService.addTheater(theaterRequest);
    }

    @GetMapping
    public List<TheaterResponse> getAllTheaters() {
        return theaterService.getAllTheaters();
    }

    @GetMapping("/{theaterId}")
    public TheaterResponse getTheaterById(@PathVariable Long theaterId) {
        return theaterService.getTheaterById(theaterId);
    }

    @PutMapping("/{theaterId}")
    public TheaterResponse updateTheater(@PathVariable Long theaterId,
                                         @Valid @RequestBody TheaterRequest theaterRequest) {
        return theaterService.updateTheater(theaterId, theaterRequest);
    }

    @DeleteMapping("/{theaterId}")
    public String deleteTheater(@PathVariable Long theaterId) {
        theaterService.deleteTheater(theaterId);
        return "Theater Deleted Successfully";
    }
}