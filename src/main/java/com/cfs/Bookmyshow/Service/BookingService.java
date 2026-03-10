package com.cfs.Bookmyshow.Service;

import com.cfs.Bookmyshow.DTO.*;
import com.cfs.Bookmyshow.Exception.ResouceNotFoundException;
import com.cfs.Bookmyshow.Exception.SeatUnavailableException;
import com.cfs.Bookmyshow.Repository.BookingRepo;
import com.cfs.Bookmyshow.Repository.ShowRepo;
import com.cfs.Bookmyshow.Repository.ShowSeatRepo;
import com.cfs.Bookmyshow.Repository.UserRepo;
import com.cfs.Bookmyshow.Repository.paymentRepo;
import com.cfs.Bookmyshow.model.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired private UserRepo userRepo;
    @Autowired private ShowRepo showRepo;
    @Autowired private ShowSeatRepo showSeatRepo;
    @Autowired private BookingRepo bookingRepo;
    @Autowired private paymentRepo paymentRepo;

    @Transactional
    public BookingDto createBooking(BookingrequestDto bookingrequest) {
        User user = userRepo.findById(bookingrequest.getUserId())
                .orElseThrow(() -> new ResouceNotFoundException("User not found"));
        Show show = showRepo.findById(bookingrequest.getShowId())
                .orElseThrow(() -> new ResouceNotFoundException("Show Not found!"));

        List<ShowSeat> showSeatList = showSeatRepo.findAllById(bookingrequest.getSeatId());

        for (ShowSeat seat : showSeatList) {
            if (!"AVAILABLE".equals(seat.getStatus())) {
                throw new SeatUnavailableException("Seat " + seat.getSeat().getId() + " is not available");
            }
            seat.setStatus("LOCKED");
        }
        showSeatRepo.saveAll(showSeatList);

        double totalAmount = showSeatList.stream().mapToDouble(ShowSeat::getBaseprice).sum();

        Payment payment = new Payment();
        payment.setStatus("SUCCESS");
        payment.setPaymentMethod(bookingrequest.getPaymentMethod());
        payment.setAmount(totalAmount);
        payment.setTransactionId(UUID.randomUUID().toString());
        payment.setTransactionTime(LocalDateTime.now());
        // ✅ Save Payment first so it has an ID before Booking references it
        Payment savedPayment = paymentRepo.save(payment);

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setShow(show);
        booking.setBookingtime(LocalDateTime.now());
        booking.setStatus("CONFIRMED");
        booking.setBookingNumber("BMS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        booking.setTotalAmount(totalAmount);
        booking.setPayment(savedPayment);

        Booking saved = bookingRepo.save(booking);

        showSeatList.forEach(seat -> {
            seat.setStatus("BOOKED");
            seat.setBooking(saved);
        });
        showSeatRepo.saveAll(showSeatList);
        return mapToBookingDTO(saved, showSeatList);
    }

    public BookingDto getBookingByID(Long id) {
        Booking booking = bookingRepo.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Booking Not Found!"));
        // ✅ FIX: use findByBookingId() — not comparing Booking object to Long
        List<ShowSeat> seats = showSeatRepo.findByBookingId(booking.getId());
        return mapToBookingDTO(booking, seats);
    }

    public BookingDto getBookingByNumber(String number) {
        Booking booking = bookingRepo.findByBookingNumber(number)
                .orElseThrow(() -> new ResouceNotFoundException("Booking Not Found!"));
        // ✅ FIX: use findByBookingId() — old code compared Booking object to Long (always false)
        List<ShowSeat> seats = showSeatRepo.findByBookingId(booking.getId());
        return mapToBookingDTO(booking, seats);
    }

    public List<BookingDto> getBookingByUserId(Long userId) {
        return bookingRepo.findByUserId(userId).stream()
                .map(booking -> {
                    // ✅ FIX: use findByBookingId() — not comparing object to Long
                    List<ShowSeat> seats = showSeatRepo.findByBookingId(booking.getId());
                    return mapToBookingDTO(booking, seats);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public BookingDto cancelBooking(Long id) {
        Booking booking = bookingRepo.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Booking Not Found!"));
        booking.setStatus("CANCELLED");

        // ✅ FIX: use findByBookingId() — not comparing Booking object to Long
        List<ShowSeat> seats = showSeatRepo.findByBookingId(booking.getId());
        seats.forEach(seat -> {
            seat.setBooking(null);
            seat.setStatus("AVAILABLE");
        });

        if (booking.getPayment() != null) {
            booking.getPayment().setStatus("REFUND");
        }

        Booking saved = bookingRepo.save(booking);
        showSeatRepo.saveAll(seats);
        return mapToBookingDTO(saved, seats);
    }

    // ─────────────────────────────────────────────────────────────────
    private BookingDto mapToBookingDTO(Booking b, List<ShowSeat> showSeatList) {
        BookingDto dto = new BookingDto();
        dto.setId(b.getId());
        dto.setBookingNumber(b.getBookingNumber());
        dto.setStatus(b.getStatus());
        dto.setTotalAmount(b.getTotalAmount());

        // User
        UserDto userDto = new UserDto();
        userDto.setId(b.getUser().getId());
        userDto.setEmail(b.getUser().getEmail());
        userDto.setUsername(b.getUser().getName());
        userDto.setPhoneNumber(b.getUser().getPhonenumber());
        dto.setUser(userDto);

        // Show → Movie → Screen → Theater
        ShowDto showDto = new ShowDto();
        showDto.setId(b.getShow().getId());
        showDto.setStartTime(b.getShow().getStartTime());
        showDto.setEndTime(b.getShow().getEndTime());

        MovieDto movieDto = new MovieDto();
        movieDto.setId(b.getShow().getMovie().getId());
        movieDto.setMovieName(b.getShow().getMovie().getTitle());
        movieDto.setDuration(b.getShow().getMovie().getDurations());
        movieDto.setGenre(b.getShow().getMovie().getGenre());
        movieDto.setPosterUrl(b.getShow().getMovie().getPosterUrl());
        movieDto.setBannerUrl(b.getShow().getMovie().getBannerUrl());
        movieDto.setLanguage(b.getShow().getMovie().getLanguage());
        movieDto.setRealeseDate(b.getShow().getMovie().getReleaseDate());
        movieDto.setDescription(b.getShow().getMovie().getDescription());
        showDto.setMovie(movieDto);

        TheaterDto theaterDto = new TheaterDto();
        theaterDto.setId(b.getShow().getScreen().getTheater().getId());
        theaterDto.setCity(b.getShow().getScreen().getTheater().getCity());
        theaterDto.setName(b.getShow().getScreen().getTheater().getName());
        theaterDto.setAddress(b.getShow().getScreen().getTheater().getAddress());
        theaterDto.setTotalScreen(b.getShow().getScreen().getTheater().getTotalScreen());

        ScreenDto screenDto = new ScreenDto();
        screenDto.setId(b.getShow().getScreen().getId());
        screenDto.setScreenName(b.getShow().getScreen().getScreenName());
        screenDto.setTotalSeats(b.getShow().getScreen().getTotalseat());
        screenDto.setTheater(theaterDto);
        showDto.setScreen(screenDto);
        dto.setShow(showDto);

        // Seats
        List<ShowSeatDto> seatDtos = showSeatList.stream().map(seat -> {
            ShowSeatDto sd = new ShowSeatDto();
            sd.setId(seat.getId());
            sd.setStatus(seat.getStatus());
            sd.setPrice(seat.getBaseprice());
            if (seat.getSeat() != null) {
                seatDto s = new seatDto();
                s.setId(seat.getSeat().getId());
                s.setSeatNumber(seat.getSeat().getSeatNumber());
                s.setBaseprice(seat.getSeat().getSeatprice());
                s.setSeattype(seat.getSeat().getSeatType());
                sd.setSeat(s);
            }
            return sd;
        }).collect(Collectors.toList());
        dto.setShowSeat(seatDtos);

        // Payment
        if (b.getPayment() != null) {
            paymentDto pd = new paymentDto();
            pd.setId(b.getPayment().getId());
            pd.setPaymentStatus(b.getPayment().getStatus());
            pd.setPaymentTime(b.getPayment().getTransactionTime());
            pd.setPaymentMethod(b.getPayment().getPaymentMethod());
            pd.setAmount(b.getPayment().getAmount());
            pd.setTransactionId(b.getPayment().getTransactionId());
            dto.setPayment(pd);
        }

        return dto;
    }
}