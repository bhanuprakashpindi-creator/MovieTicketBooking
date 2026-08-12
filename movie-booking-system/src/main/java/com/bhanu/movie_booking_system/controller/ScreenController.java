package com.bhanu.movie_booking_system.controller;

import com.bhanu.movie_booking_system.dto.request.ScreenRequest;
import com.bhanu.movie_booking_system.dto.response.ScreenResponse;
import com.bhanu.movie_booking_system.service.ScreenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/screens")
public class ScreenController {

    @Autowired
    private ScreenService screenService;

    @PostMapping
    public ScreenResponse addScreen(@Valid @RequestBody ScreenRequest screenRequest) {
        return screenService.addScreen(screenRequest);
    }

    @GetMapping
    public List<ScreenResponse> getAllScreens() {
        return screenService.getAllScreens();
    }

    @GetMapping("/{screenId}")
    public ScreenResponse getScreenById(@PathVariable Long screenId) {
        return screenService.getScreenById(screenId);
    }

    @PutMapping("/{screenId}")
    public ScreenResponse updateScreen(@PathVariable Long screenId,
                                       @Valid @RequestBody ScreenRequest screenRequest) {
        return screenService.updateScreen(screenId, screenRequest);
    }

    @DeleteMapping("/{screenId}")
    public String deleteScreen(@PathVariable Long screenId) {
        screenService.deleteScreen(screenId);
        return "Screen Deleted Successfully";
    }
}