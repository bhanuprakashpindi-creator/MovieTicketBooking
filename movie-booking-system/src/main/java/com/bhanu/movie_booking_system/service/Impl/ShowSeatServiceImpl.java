package com.bhanu.movie_booking_system.service.Impl;

import com.bhanu.movie_booking_system.dto.response.ShowSeatResponse;
import com.bhanu.movie_booking_system.entity.Enum.SeatStatus;
import com.bhanu.movie_booking_system.entity.Seat;
import com.bhanu.movie_booking_system.entity.Screen;
import com.bhanu.movie_booking_system.entity.Show;
import com.bhanu.movie_booking_system.entity.ShowSeat;
import com.bhanu.movie_booking_system.repository.SeatRepository;
import com.bhanu.movie_booking_system.repository.ShowSeatRepository;
import com.bhanu.movie_booking_system.service.ShowSeatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShowSeatServiceImpl implements ShowSeatService {

    private static final Logger log =
            LoggerFactory.getLogger(ShowSeatServiceImpl.class);

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private ShowSeatRepository showSeatRepository;


    // =========================================================
    // GENERATE SHOW SEATS
    // =========================================================

    @Override
    public void generateShowSeats(
            Show show,
            Screen screen) {

        log.info(
                "Starting show seat generation. showId={}, screenId={}",
                show.getShowId(),
                screen.getScreenId()
        );


        List<Seat> seats =
                seatRepository.findByScreen(screen);


        log.debug(
                "Seats found for screen. screenId={}, count={}",
                screen.getScreenId(),
                seats.size()
        );


        List<ShowSeat> showSeats =
                new ArrayList<>();


        for (Seat seat : seats) {

            ShowSeat showSeat =
                    new ShowSeat();

            showSeat.setShow(show);

            showSeat.setSeat(seat);

            showSeat.setSeatStatus(
                    SeatStatus.AVAILABLE
            );

            showSeats.add(showSeat);
        }


        showSeatRepository.saveAll(
                showSeats
        );


        log.info(
                "Show seats generated successfully. showId={}, screenId={}, totalSeats={}",
                show.getShowId(),
                screen.getScreenId(),
                showSeats.size()
        );
    }


    // =========================================================
    // GET SHOW SEATS
    // =========================================================

    @Override
    public List<ShowSeatResponse> getShowSeats(
            Long showId) {

        log.info(
                "Get show seats request received. showId={}",
                showId
        );


        List<ShowSeat> showSeats =
                showSeatRepository
                        .findByShow_ShowId(showId);


        log.debug(
                "Show seats retrieved. showId={}, count={}",
                showId,
                showSeats.size()
        );


        List<ShowSeatResponse> responses =
                new ArrayList<>();


        for (ShowSeat showSeat : showSeats) {

            ShowSeatResponse response =
                    new ShowSeatResponse();

            response.setShowSeatId(
                    showSeat.getShowSeatId()
            );

            response.setSeatId(
                    showSeat.getSeat().getSeatId()
            );

            response.setSeatNumber(
                    showSeat.getSeat().getSeatNumber()
            );

            response.setSeatType(
                    showSeat.getSeat().getSeatType()
            );

            response.setSeatStatus(
                    showSeat.getSeatStatus()
            );

            responses.add(response);
        }


        log.info(
                "Show seats fetched successfully. showId={}, count={}",
                showId,
                responses.size()
        );


        return responses;
    }
}