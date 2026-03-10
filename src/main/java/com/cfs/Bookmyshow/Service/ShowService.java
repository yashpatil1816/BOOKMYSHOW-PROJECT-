package com.cfs.Bookmyshow.Service;

import com.cfs.Bookmyshow.DTO.*;
import com.cfs.Bookmyshow.Exception.ResouceNotFoundException;
import com.cfs.Bookmyshow.Repository.*;
import com.cfs.Bookmyshow.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ShowService {

    @Autowired private ShowRepo showRepo;
    @Autowired private MovieRepo movieRepo;
    @Autowired private ScreenRepo screenRepo;
    @Autowired private ShowSeatRepo showSeatRepo;
    @Autowired private SeatRepo seatRepo;   // ← needed to fetch seats of the screen

    public ShowDto createShow(ShowDto showDto) {
        Show show = new Show();

        Movie movie = movieRepo.findById(showDto.getMovie().getId())
                .orElseThrow(() -> new ResouceNotFoundException("Movie Not Found!!"));
        Screen screen = screenRepo.findById(showDto.getScreen().getId())
                .orElseThrow(() -> new ResouceNotFoundException("Screen Not Found!!"));

        show.setMovie(movie);
        show.setScreen(screen);

        // ✅ FIX 1: language from DB entity, not from the thin DTO (which has no language)
        show.setLanguage(movie.getLanguage() != null ? movie.getLanguage() : "Hindi");

        show.setStartTime(showDto.getStartTime());

        // ✅ FIX 2: Auto-calculate endTime from movie duration if not sent
        if (showDto.getEndTime() != null) {
            show.setEndTime(showDto.getEndTime());
        } else {
            show.setEndTime(calcEndTime(showDto.getStartTime(), movie.getDurations()));
        }

        Show saved = showRepo.save(show);

        // ✅ FIX 3: Create one ShowSeat (AVAILABLE) for every Seat in this screen
        //    Without this, shows always show 0 available seats!
        List<Seat> screenSeats = seatRepo.findByScreenId(screen.getId());

        List<ShowSeat> showSeats = new ArrayList<>();
        for (Seat seat : screenSeats) {
            ShowSeat ss = new ShowSeat();
            ss.setShow(saved);
            ss.setSeat(seat);
            ss.setStatus("AVAILABLE");
            // Use seat's own price, or movie base price as fallback
            double price = 0.0;
            try { price = Double.parseDouble(seat.getSeatprice()); } catch (Exception ignored) {}
            if (price == 0.0 && movie.getAmount() != null) price = movie.getAmount();
            ss.setBaseprice(price);
            ss.setBooking(null);  // no booking yet
            showSeats.add(ss);
        }
        showSeatRepo.saveAll(showSeats);

        // Return DTO with all available seats
        List<ShowSeat> availableSeats = showSeatRepo.findByShowIdAndStatus(saved.getId(), "AVAILABLE");
        return mapToDto(saved, availableSeats);
    }

    public ShowDto getShowById(Long id) {
        Show show = showRepo.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Show not found with id: " + id));
        List<ShowSeat> seats = showSeatRepo.findByShowIdAndStatus(show.getId(), "AVAILABLE");
        return mapToDto(show, seats);
    }

    public List<ShowDto> getAllShows() {
        return showRepo.findAll().stream()
                .map(show -> {
                    List<ShowSeat> seats = showSeatRepo.findByShowIdAndStatus(show.getId(), "AVAILABLE");
                    return mapToDto(show, seats);
                })
                .collect(Collectors.toList());
    }

    // Parse "2h 30m" / "2h" / "150m" → add minutes to startTime
    private LocalDateTime calcEndTime(LocalDateTime start, String duration) {
        if (start == null) return null;
        if (duration == null || duration.isBlank()) return start.plusHours(2);
        int totalMinutes = 0;
        Matcher h = Pattern.compile("(\\d+)\\s*h").matcher(duration);
        Matcher m = Pattern.compile("(\\d+)\\s*m").matcher(duration);
        if (h.find()) totalMinutes += Integer.parseInt(h.group(1)) * 60;
        if (m.find()) totalMinutes += Integer.parseInt(m.group(1));
        if (totalMinutes == 0) totalMinutes = 120;
        return start.plusMinutes(totalMinutes);
    }

    // Reads from the saved entity — NOT from the DTO being constructed
    private ShowDto mapToDto(Show savedshow, List<ShowSeat> showSeatList) {
        ShowDto dto = new ShowDto();
        dto.setId(savedshow.getId());
        dto.setStartTime(savedshow.getStartTime());
        dto.setEndTime(savedshow.getEndTime());

        Movie m = savedshow.getMovie();
        if (m != null) {
            MovieDto movieDto = new MovieDto();
            movieDto.setId(m.getId());
            movieDto.setMovieName(m.getTitle());
            movieDto.setLanguage(m.getLanguage());
            movieDto.setDuration(m.getDurations());
            movieDto.setGenre(m.getGenre());
            movieDto.setDescription(m.getDescription());
            movieDto.setPosterUrl(m.getPosterUrl());
            movieDto.setBannerUrl(m.getBannerUrl());
            movieDto.setRealeseDate(m.getReleaseDate());
            movieDto.setAmount(m.getAmount());
            dto.setMovie(movieDto);
        }

        Screen sc = savedshow.getScreen();
        if (sc != null) {
            TheaterDto theaterDto = null;
            if (sc.getTheater() != null) {
                theaterDto = new TheaterDto();
                theaterDto.setId(sc.getTheater().getId());
                theaterDto.setName(sc.getTheater().getName());
                theaterDto.setCity(sc.getTheater().getCity());
                theaterDto.setAddress(sc.getTheater().getAddress());
                theaterDto.setTotalScreen(sc.getTheater().getTotalScreen());
            }
            ScreenDto screenDto = new ScreenDto();
            screenDto.setId(sc.getId());
            screenDto.setScreenName(sc.getScreenName());
            screenDto.setTotalSeats(sc.getTotalseat());
            screenDto.setTheater(theaterDto);
            dto.setScreen(screenDto);
        }

        List<ShowSeatDto> seatDtos = showSeatList.stream().map(seat -> {
            ShowSeatDto sd = new ShowSeatDto();
            sd.setId(seat.getId());
            sd.setStatus(seat.getStatus());
            sd.setPrice(seat.getBaseprice());
            if (seat.getSeat() != null) {
                seatDto sDto = new seatDto();
                sDto.setId(seat.getSeat().getId());
                sDto.setSeatNumber(seat.getSeat().getSeatNumber());
                sDto.setSeattype(seat.getSeat().getSeatType());
                sDto.setBaseprice(seat.getSeat().getSeatprice());
                sd.setSeat(sDto);
            }
            return sd;
        }).collect(Collectors.toList());

        dto.setAvailableSeat(seatDtos);
        return dto;
    }
}