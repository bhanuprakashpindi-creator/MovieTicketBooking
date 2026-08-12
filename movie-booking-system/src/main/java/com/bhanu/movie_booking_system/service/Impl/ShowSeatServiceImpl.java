package com.bhanu.movie_booking_system.service.Impl;

import com.bhanu.movie_booking_system.entity.Enum.SeatStatus;
import com.bhanu.movie_booking_system.entity.Seat;
import com.bhanu.movie_booking_system.entity.Screen;
import com.bhanu.movie_booking_system.entity.Show;
import com.bhanu.movie_booking_system.entity.ShowSeat;
import com.bhanu.movie_booking_system.repository.SeatRepository;
import com.bhanu.movie_booking_system.repository.ShowSeatRepository;
import com.bhanu.movie_booking_system.service.ShowSeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShowSeatServiceImpl implements ShowSeatService {

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private ShowSeatRepository showSeatRepository;

    @Override
    public void generateShowSeats(Show show, Screen screen) {

        List<Seat> seats = seatRepository.findByScreen(screen);

        List<ShowSeat> showSeats = new ArrayList<>();

        for (Seat seat : seats) {

            ShowSeat showSeat = new ShowSeat();

            showSeat.setShow(show);
            showSeat.setSeat(seat);
            showSeat.setSeatStatus(SeatStatus.AVAILABLE);

            showSeats.add(showSeat);
        }

        showSeatRepository.saveAll(showSeats);
    }
}