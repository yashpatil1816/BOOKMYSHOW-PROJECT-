package com.cfs.Bookmyshow.Controller;

import com.cfs.Bookmyshow.DTO.BookingDto;
import com.cfs.Bookmyshow.DTO.BookingrequestDto;
import com.cfs.Bookmyshow.Service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booking")
@CrossOrigin(origins = "*")
public class BookingController {

    @Autowired
    BookingService bookingService;

    // POST /api/booking/create
    @PostMapping("/create")
    public ResponseEntity<BookingDto> createBooking(@RequestBody BookingrequestDto bookingrequestDto) {
        return new ResponseEntity<>(bookingService.createBooking(bookingrequestDto), HttpStatus.CREATED);
    }

    // GET /api/booking/{id}
    @GetMapping("/{id}")
    public ResponseEntity<BookingDto> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingByID(id));
    }

    // GET /api/booking/number/{bookingNumber}  ← used by bookings.html lookup
    @GetMapping("/number/{bookingNumber}")
    public ResponseEntity<BookingDto> getByBookingNumber(@PathVariable String bookingNumber) {
        return ResponseEntity.ok(bookingService.getBookingByNumber(bookingNumber));
    }

    // GET /api/booking/user/{userId}  ← used by bookings.html and admin
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingDto>> getByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(bookingService.getBookingByUserId(userId));
    }

    // PUT /api/booking/cancel/{id}  ← used by cancel button
    @PutMapping("/cancel/{id}")
    public ResponseEntity<BookingDto> cancelBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.cancelBooking(id));
    }
}