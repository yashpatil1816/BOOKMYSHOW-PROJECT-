package com.cfs.Bookmyshow.Service;

import com.cfs.Bookmyshow.DTO.MovieDto;
import com.cfs.Bookmyshow.Exception.ResouceNotFoundException;
import com.cfs.Bookmyshow.Repository.*;
import com.cfs.Bookmyshow.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {

    @Autowired private MovieRepo movieRepo;
    @Autowired private ShowRepo showRepo;
    @Autowired private ShowSeatRepo showSeatRepo;
    @Autowired private BookingRepo bookingRepo;

    public MovieDto createMovie(MovieDto movieDto) {
        Movie movie = mapToEntity(movieDto);
        return mapToDTO(movieRepo.save(movie));
    }

    public MovieDto getMovieById(Long id) {
        return mapToDTO(movieRepo.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Movie not found with id : " + id)));
    }

    public List<MovieDto> getAllMovies() {
        return movieRepo.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<MovieDto> getMovieByLanguage(String language) {
        return movieRepo.findByLanguage(language).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<MovieDto> searchMovies(String title) {
        return movieRepo.findByTitleContaining(title).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public MovieDto updateMovie(Long id, MovieDto movieDto) {
        Movie movie = movieRepo.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Movie not found with id : " + id));
        movie.setTitle(movieDto.getMovieName());
        movie.setDescription(movieDto.getDescription());
        movie.setLanguage(movieDto.getLanguage());
        movie.setGenre(movieDto.getGenre());
        movie.setDurations(movieDto.getDuration());
        movie.setReleaseDate(movieDto.getRealeseDate());
        movie.setPosterUrl(movieDto.getPosterUrl());
        movie.setBannerUrl(movieDto.getBannerUrl());
        movie.setAmount(movieDto.getAmount());
        return mapToDTO(movieRepo.save(movie));
    }

    /**
     * FIX for TransientObjectException:
     *
     * Root cause: Movie has CascadeType.ALL on List<Show>.
     * Hibernate tries to cascade-delete Shows, but Show.screen is a
     * @ManyToOne not in the cascade path — Hibernate hits it as a
     * "transient unsaved instance" during flush and throws.
     *
     * Fix: Manually delete in FK-safe order BEFORE touching Movie:
     *   ShowSeats (null booking FK first) → Bookings → Shows → Movie
     */
    @Transactional
    public void deleteMovie(Long id) {
        Movie movie = movieRepo.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Movie not found with id : " + id));

        List<Show> shows = showRepo.findByMovieId(id);

        for (Show show : shows) {
            // Null the booking FK on ShowSeats before deleting (avoids constraint violation)
            List<ShowSeat> seats = showSeatRepo.findByShowId(show.getId());
            seats.forEach(s -> s.setBooking(null));
            showSeatRepo.saveAll(seats);
            showSeatRepo.deleteAll(seats);

            // Delete bookings for this show
            bookingRepo.deleteAll(bookingRepo.findByShowId(show.getId()));
        }

        // Delete all shows (now safe — no child FK references remain)
        showRepo.deleteAll(shows);

        // Finally delete the movie
        movieRepo.delete(movie);
    }

    // ─────────────────────────────────────────
    private MovieDto mapToDTO(Movie movie) {
        MovieDto d = new MovieDto();
        d.setId(movie.getId());
        d.setMovieName(movie.getTitle());
        d.setDescription(movie.getDescription());
        d.setLanguage(movie.getLanguage());
        d.setGenre(movie.getGenre());
        d.setDuration(movie.getDurations());
        d.setRealeseDate(movie.getReleaseDate());
        d.setPosterUrl(movie.getPosterUrl());
        d.setBannerUrl(movie.getBannerUrl());
        d.setAmount(movie.getAmount());
        return d;
    }

    private Movie mapToEntity(MovieDto d) {
        Movie m = new Movie();
        m.setTitle(d.getMovieName());
        m.setDurations(d.getDuration());
        m.setDescription(d.getDescription());
        m.setGenre(d.getGenre());
        m.setLanguage(d.getLanguage());
        m.setPosterUrl(d.getPosterUrl());
        m.setBannerUrl(d.getBannerUrl());
        m.setReleaseDate(d.getRealeseDate());
        m.setAmount(d.getAmount());
        return m;
    }
}