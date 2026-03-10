package com.cfs.Bookmyshow.Service;

import com.cfs.Bookmyshow.DTO.ScreenDto;
import com.cfs.Bookmyshow.DTO.TheaterDto;
import com.cfs.Bookmyshow.Exception.ResouceNotFoundException;
import com.cfs.Bookmyshow.Repository.ScreenRepo;
import com.cfs.Bookmyshow.Repository.TheaterRepo;
import com.cfs.Bookmyshow.model.Screen;
import com.cfs.Bookmyshow.model.Theater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScreenService {

    @Autowired private ScreenRepo screenRepo;
    @Autowired private TheaterRepo theaterRepo;

    public ScreenDto createScreen(ScreenDto screenDto) {
        Theater theater = theaterRepo.findById(screenDto.getTheater().getId())
                .orElseThrow(() -> new ResouceNotFoundException("Theater not found: " + screenDto.getTheater().getId()));

        Screen screen = new Screen();
        screen.setScreenName(screenDto.getScreenName());
        screen.setTotalseat(screenDto.getTotalSeats());
        screen.setScreenType(screenDto.getScreenType() != null ? screenDto.getScreenType() : "STANDARD");
        screen.setTheater(theater);

        return mapToDto(screenRepo.save(screen));
    }

    public ScreenDto getScreenById(Long id) {
        return mapToDto(screenRepo.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Screen not found: " + id)));
    }

    public List<ScreenDto> getAllScreens() {
        return screenRepo.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public List<ScreenDto> getScreensByTheater(Long theaterId) {
        return screenRepo.findByTheaterId(theaterId).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public void deleteScreen(Long id) {
        screenRepo.delete(screenRepo.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Screen not found: " + id)));
    }

    private ScreenDto mapToDto(Screen screen) {
        ScreenDto dto = new ScreenDto();
        dto.setId(screen.getId());
        dto.setScreenName(screen.getScreenName());
        dto.setTotalSeats(screen.getTotalseat());
        dto.setScreenType(screen.getScreenType() != null ? screen.getScreenType() : "STANDARD");

        if (screen.getTheater() != null) {
            TheaterDto t = new TheaterDto();
            t.setId(screen.getTheater().getId());
            t.setName(screen.getTheater().getName());
            t.setCity(screen.getTheater().getCity());
            t.setAddress(screen.getTheater().getAddress());
            t.setTotalScreen(screen.getTheater().getTotalScreen());
            dto.setTheater(t);
        }
        return dto;
    }
}