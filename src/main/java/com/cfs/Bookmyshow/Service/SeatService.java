package com.cfs.Bookmyshow.Service;

import com.cfs.Bookmyshow.DTO.seatDto;
import com.cfs.Bookmyshow.Exception.ResouceNotFoundException;
import com.cfs.Bookmyshow.Repository.ScreenRepo;
import com.cfs.Bookmyshow.Repository.SeatRepo;
import com.cfs.Bookmyshow.model.Screen;
import com.cfs.Bookmyshow.model.Seat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeatService {

    @Autowired private SeatRepo seatRepo;
    @Autowired private ScreenRepo screenRepo;

    // Get all seats for a screen
    public List<seatDto> getSeatsByScreen(Long screenId) {
        return seatRepo.findByScreenId(screenId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    // Add a single seat to a screen
    public seatDto addSeat(Long screenId, seatDto dto) {
        Screen screen = screenRepo.findById(screenId)
                .orElseThrow(() -> new ResouceNotFoundException("Screen not found: " + screenId));
        Seat seat = new Seat();
        seat.setScreen(screen);
        seat.setSeatNumber(dto.getSeatNumber());
        seat.setSeatType(dto.getSeattype());
        seat.setSeatprice(dto.getBaseprice());
        return toDto(seatRepo.save(seat));
    }

    // Auto-generate seats for a screen (called when screen is created)
    // Creates rows A-Z with seat numbers A1, A2... up to totalSeats
    public List<seatDto> generateSeats(Long screenId, int totalSeats) {
        Screen screen = screenRepo.findById(screenId)
                .orElseThrow(() -> new ResouceNotFoundException("Screen not found: " + screenId));

        // Delete existing seats for this screen first (re-generate)
        List<Seat> existing = seatRepo.findByScreenId(screenId);
        seatRepo.deleteAll(existing);

        List<Seat> seats = new ArrayList<>();
        int seatsPerRow = 10;
        int row = 0;
        for (int i = 0; i < totalSeats; i++) {
            char rowLetter = (char) ('A' + (i / seatsPerRow));
            int seatNum = (i % seatsPerRow) + 1;
            Seat seat = new Seat();
            seat.setScreen(screen);
            seat.setSeatNumber(rowLetter + String.valueOf(seatNum));
            // First 2 rows = RECLINER, next 3 = GOLD, rest = NORMAL
            if (row < 2)      { seat.setSeatType("RECLINER"); seat.setSeatprice("600"); }
            else if (row < 5) { seat.setSeatType("GOLD");     seat.setSeatprice("350"); }
            else              { seat.setSeatType("NORMAL");   seat.setSeatprice("200"); }
            seats.add(seat);
            if ((i + 1) % seatsPerRow == 0) row++;
        }
        return seatRepo.saveAll(seats).stream().map(this::toDto).collect(Collectors.toList());
    }

    // Delete a single seat
    public void deleteSeat(Long seatId) {
        seatRepo.deleteById(seatId);
    }

    private seatDto toDto(Seat s) {
        seatDto d = new seatDto();
        d.setId(s.getId());
        d.setSeatNumber(s.getSeatNumber());
        d.setSeattype(s.getSeatType());
        d.setBaseprice(s.getSeatprice());
        return d;
    }
}