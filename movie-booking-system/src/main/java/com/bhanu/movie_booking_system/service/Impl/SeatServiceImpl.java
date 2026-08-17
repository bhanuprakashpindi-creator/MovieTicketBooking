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
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import java.util.List;

    @Service
    public class SeatServiceImpl implements SeatService {

        @Autowired
        private SeatRepository seatRepository;

        @Autowired
        private ScreenRepository screenRepository;

        @Override
        public SeatResponse addSeat(SeatRequest seatRequest) {

            Screen screen = screenRepository.findById(seatRequest.getScreenId())
                    .orElseThrow(() ->
                            new RuntimeException("Screen Not Found"));

            Seat seat =
                    SeatTransformer.seatRequestToSeat(seatRequest, screen);

            Seat savedSeat = seatRepository.save(seat);

            return SeatTransformer.seatToSeatResponse(savedSeat);
        }

        @Override
        public List<SeatResponse> getAllSeats() {

            return seatRepository.findAll()
                    .stream()
                    .map(SeatTransformer::seatToSeatResponse)
                    .toList();
        }

        @Override
        public SeatResponse getSeatById(Long seatId) {

            Seat seat = seatRepository.findById(seatId)
                    .orElseThrow(() ->
                            new RuntimeException("Seat Not Found"));

            return SeatTransformer.seatToSeatResponse(seat);
        }

        @Override
        public SeatResponse updateSeat(Long seatId,
                                       SeatRequest seatRequest) {

            Seat seat = seatRepository.findById(seatId)
                    .orElseThrow(() ->
                            new RuntimeException("Seat Not Found"));

            Screen screen = screenRepository.findById(seatRequest.getScreenId())
                    .orElseThrow(() ->
                            new RuntimeException("Screen Not Found"));

            seat.setSeatNumber(seatRequest.getSeatNumber());
            seat.setSeatType(seatRequest.getSeatType());
            seat.setScreen(screen);

            Seat updatedSeat = seatRepository.save(seat);

            return SeatTransformer.seatToSeatResponse(updatedSeat);
        }

        @Override
        public void deleteSeat(Long seatId) {

            Seat seat = seatRepository.findById(seatId)
                    .orElseThrow(() ->
                            new RuntimeException("Seat Not Found"));

            seatRepository.delete(seat);
        }
        @Override
        public void generateSeats(Screen screen) {

            for (int i = 1; i <= screen.getTotalSeats(); i++) {

                Seat seat = new Seat();

                seat.setSeatNumber("A" + i);
                seat.setSeatType(SeatType.REGULAR);
                seat.setScreen(screen);

                seatRepository.save(seat);
            }
        }
    }