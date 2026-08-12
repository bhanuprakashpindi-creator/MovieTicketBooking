package com.bhanu.movie_booking_system.controller;

import com.bhanu.movie_booking_system.dto.request.ShowRequest;
import com.bhanu.movie_booking_system.dto.response.ShowResponse;
import com.bhanu.movie_booking_system.service.ShowService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shows")
public class ShowController {

    @Autowired
    private ShowService showService;

    @PostMapping
    public ShowResponse addShow(@Valid @RequestBody ShowRequest showRequest) {
        return showService.addShow(showRequest);
    }

    @GetMapping
    public List<ShowResponse> getAllShows() {
        return showService.getAllShows();
    }

    @GetMapping("/{showId}")
    public ShowResponse getShowById(@PathVariable Long showId) {
        return showService.getShowById(showId);
    }

    @PutMapping("/{showId}")
    public ShowResponse updateShow(@PathVariable Long showId,
                                   @Valid @RequestBody ShowRequest showRequest) {
        return showService.updateShow(showId, showRequest);
    }

    @DeleteMapping("/{showId}")
    public String deleteShow(@PathVariable Long showId) {
        showService.deleteShow(showId);
        return "Show Deleted Successfully";
    }
}