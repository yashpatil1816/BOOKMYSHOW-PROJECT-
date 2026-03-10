package com.cfs.Bookmyshow.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private Long id;
    private String bookingNumber;
    private LocalDateTime startTime;
    private Double totalAmount;   // ✅ FIX: was 'totalamount' (lowercase a) → JSON sent as 'totalamount', frontend reads 'totalAmount'
    private ShowDto show;
    private UserDto user;
    private String status;
    private List<ShowSeatDto> showSeat;
    private paymentDto payment;
}