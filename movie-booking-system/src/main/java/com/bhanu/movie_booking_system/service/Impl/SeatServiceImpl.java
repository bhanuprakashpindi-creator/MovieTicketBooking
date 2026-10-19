package com.bhanu.movie_booking_system.service.Impl;

import com.bhanu.movie_booking_system.dto.request.SeatRequest;
import com.bhanu.movie_booking_system.dto.response.SeatResponse;
import com.bhanu.movie_booking_system.entity.Enum.SeatType;
import com.bhanu.movie_booking_system.entity.Screen;
import com.bhanu.movie_booking_system.entity.Seat;
import com.bhanu.movie_booking_system.repository.ScreenRepository;
import com.bhanu.movie_booking_system.repository.SeatRepository;
import com.bhanu.movie_booking_system.service.SeatService;
import com.bhanu.movie_booking_system.transformer.SeatTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatServiceImpl implements SeatService {

    private static final Logger log =
            LoggerFactory.getLogger(SeatServiceImpl.class);

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private ScreenRepository screenRepository;


    // =========================================================
    // ADD SEAT
    // =========================================================

    @Override
    public SeatResponse addSeat(SeatRequest seatRequest) {

        log.info(
                "Seat creation request received. seatNumber={}, screenId={}",
                seatRequest.getSeatNumber(),
                seatRequest.getScreenId()
        );


        Screen screen =
                screenRepository.findById(
                                seatRequest.getScreenId()
                        )
                        .orElseThrow(() -> {

                            log.warn(
                                    "Seat creation failed. Screen not found. screenId={}",
                                    seatRequest.getScreenId()
                            );

                            return new RuntimeException(
                                    "Screen Not Found"
                            );
                        });


        Seat seat =
                SeatTransformer.seatRequestToSeat(
                        seatRequest,
                        screen
                );


        Seat savedSeat =
                seatRepository.save(seat);


        log.info(
                "Seat created successfully. seatId={}, seatNumber={}, screenId={}",
                savedSeat.getSeatId(),
                savedSeat.getSeatNumber(),
                screen.getScreenId()
        );


        return SeatTransformer.seatToSeatResponse(
                savedSeat
        );
    }


    // =========================================================
    // GET ALL SEATS
    // =========================================================

    @Override
    public List<SeatResponse> getAllSeats() {

        log.info(
                "Get all seats request received"
        );


        List<SeatResponse> seats =
                seatRepository.findAll()
                        .stream()
                        .map(SeatTransformer::seatToSeatResponse)
                        .toList();


        log.info(
                "All seats fetched successfully. count={}",
                seats.size()
        );


        return seats;
    }


    // =========================================================
    // GET SEAT BY ID
    // =========================================================

    @Override
    public SeatResponse getSeatById(Long seatId) {

        log.info(
                "Get seat request received. seatId={}",
                seatId
        );


        Seat seat =
                seatRepository.findById(seatId)
                        .orElseThrow(() -> {

                            log.warn(
                                    "Seat not found. seatId={}",
                                    seatId
                            );

                            return new RuntimeException(
                                    "Seat Not Found"
                            );
                        });


        log.info(
                "Seat fetched successfully. seatId={}, seatNumber={}",
                seatId,
                seat.getSeatNumber()
        );


        return SeatTransformer.seatToSeatResponse(
                seat
        );
    }


    // =========================================================
    // UPDATE SEAT
    // =========================================================

    @Override
    public SeatResponse updateSeat(
            Long seatId,
            SeatRequest seatRequest) {

        log.info(
                "Seat update request received. seatId={}, screenId={}",
                seatId,
                seatRequest.getScreenId()
        );


        Seat seat =
                seatRepository.findById(seatId)
                        .orElseThrow(() -> {

                            log.warn(
                                    "Seat update failed. Seat not found. seatId={}",
                                    seatId
                            );

                            return new RuntimeException(
                                    "Seat Not Found"
                            );
                        });


        Screen screen =
                screenRepository.findById(
                                seatRequest.getScreenId()
                        )
                        .orElseThrow(() -> {

                            log.warn(
                                    "Seat update failed. Screen not found. screenId={}",
                                    seatRequest.getScreenId()
                            );

                            return new RuntimeException(
                                    "Screen Not Found"
                            );
                        });


        seat.setSeatNumber(
                seatRequest.getSeatNumber()
        );

        seat.setSeatType(
                seatRequest.getSeatType()
        );

        seat.setScreen(
                screen
        );


        Seat updatedSeat =
                seatRepository.save(seat);


        log.info(
                "Seat updated successfully. seatId={}, seatNumber={}, screenId={}",
                updatedSeat.getSeatId(),
                updatedSeat.getSeatNumber(),
                screen.getScreenId()
        );


        return SeatTransformer.seatToSeatResponse(
                updatedSeat
        );
    }


    // =========================================================
    // DELETE SEAT
    // =========================================================

    @Override
    public void deleteSeat(Long seatId) {

        log.info(
                "Seat deletion request received. seatId={}",
                seatId
        );


        Seat seat =
                seatRepository.findById(seatId)
                        .orElseThrow(() -> {

                            log.warn(
                                    "Seat deletion failed. Seat not found. seatId={}",
                                    seatId
                            );

                            return new RuntimeException(
                                    "Seat Not Found"
                            );
                        });


        seatRepository.delete(seat);


        log.info(
                "Seat deleted successfully. seatId={}, seatNumber={}",
                seatId,
                seat.getSeatNumber()
        );
    }


    // =========================================================
    // GENERATE SEATS
    // =========================================================

    @Override
    public void generateSeats(Screen screen) {

        log.info(
                "Starting seat generation. screenId={}, totalSeats={}",
                screen.getScreenId(),
                screen.getTotalSeats()
        );


        for (int i = 1;
             i <= screen.getTotalSeats();
             i++) {

            Seat seat = new Seat();

            seat.setSeatNumber("A" + i);
            seat.setSeatType(SeatType.REGULAR);
            seat.setScreen(screen);

            seatRepository.save(seat);
        }


        log.info(
                "Seat generation completed. screenId={}, totalSeats={}",
                screen.getScreenId(),
                screen.getTotalSeats()
        );
    }
    @Override
    public List<SeatResponse> getSeatsByScreen(Long screenId) {

        Screen screen = screenRepository.findById(screenId)
                .orElseThrow(() -> new RuntimeException("Screen not found"));

        List<Seat> seats = seatRepository.findByScreen(screen);

        return seats.stream()
                .map(SeatTransformer::seatToSeatResponse)
                .toList();
    }
}