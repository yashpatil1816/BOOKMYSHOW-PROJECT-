package com.cfs.Bookmyshow.Controller;

import com.cfs.Bookmyshow.DTO.seatDto;
import com.cfs.Bookmyshow.Service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seat")
@CrossOrigin(origins = "*")
public class SeatController {

    @Autowired SeatService seatService;

    // GET /api/seat/screen/{screenId}  — list all seats for a screen
    @GetMapping("/screen/{screenId}")
    public ResponseEntity<List<seatDto>> getByScreen(@PathVariable Long screenId) {
        return ResponseEntity.ok(seatService.getSeatsByScreen(screenId));
    }

    // POST /api/seat/screen/{screenId}/add  — add one seat manually
    @PostMapping("/screen/{screenId}/add")
    public ResponseEntity<seatDto> addSeat(@PathVariable Long screenId, @RequestBody seatDto dto) {
        return ResponseEntity.ok(seatService.addSeat(screenId, dto));
    }

    // POST /api/seat/screen/{screenId}/generate?total=100  — auto-generate all seats
    @PostMapping("/screen/{screenId}/generate")
    public ResponseEntity<List<seatDto>> generateSeats(
            @PathVariable Long screenId,
            @RequestParam(defaultValue = "100") int total) {
        return ResponseEntity.ok(seatService.generateSeats(screenId, total));
    }

    // DELETE /api/seat/{seatId}  — delete a seat
    @DeleteMapping("/{seatId}")
    public ResponseEntity<String> deleteSeat(@PathVariable Long seatId) {
        seatService.deleteSeat(seatId);
        return ResponseEntity.ok("Seat deleted");
    }
}