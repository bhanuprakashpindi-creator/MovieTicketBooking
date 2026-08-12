package com.bhanu.movie_booking_system.service.Impl;

import com.bhanu.movie_booking_system.entity.Enum.SeatType;
import com.bhanu.movie_booking_system.entity.Screen;
import com.bhanu.movie_booking_system.entity.Seat;
import com.bhanu.movie_booking_system.repository.SeatRepository;
import com.bhanu.movie_booking_system.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SeatServiceImpl implements SeatService {

    @Autowired
    private SeatRepository seatRepository;
    @Override
    public void generateSeats(Screen screen) {

        List<Seat> seats = new ArrayList<>();

        int totalSeats = screen.getTotalSeats();

        int seatsPerRow = 10;

        int totalRows = (int) Math.ceil((double) totalSeats / seatsPerRow);

        char row = 'A';

        int seatCount = 0;

        for (int i = 0; i < totalRows; i++) {

            for (int j = 1; j <= seatsPerRow && seatCount < totalSeats; j++) {

                Seat seat = new Seat();

                seat.setSeatNumber(row + String.valueOf(j));

                if (row >= 'A' && row <= 'F') {
                    seat.setSeatType(SeatType.REGULAR);
                }
                else if (row >= 'G' && row <= 'I') {
                    seat.setSeatType(SeatType.PREMIUM);
                }
                else {
                    seat.setSeatType(SeatType.RECLINER);
                }

                seat.setScreen(screen);

                seats.add(seat);

                seatCount++;
            }

            row++;
        }

        seatRepository.saveAll(seats);
    }
}