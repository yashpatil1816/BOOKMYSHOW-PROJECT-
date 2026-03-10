package com.cfs.Bookmyshow.Service;

import com.cfs.Bookmyshow.DTO.TheaterDto;
import com.cfs.Bookmyshow.Exception.ResouceNotFoundException;
import com.cfs.Bookmyshow.Repository.TheaterRepo;
import com.cfs.Bookmyshow.model.Theater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TheatherService {

    @Autowired
    private TheaterRepo theaterRepo;

    public TheaterDto craeteTheater(TheaterDto theaterDto){
        Theater theater = mapToentity(theaterDto);
        Theater savetheater = theaterRepo.save(theater);
        return mapToDto(savetheater);
    }

    public TheaterDto getTheaterById(Long id){
        Theater theater = theaterRepo.findById(id)
                .orElseThrow(()->new ResouceNotFoundException("Theater Not Found !! "));
        return mapToDto(theater);
    }

    public List<TheaterDto> getALLTheater(){
        List<Theater> theater = theaterRepo.findAll();
        return theater.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<TheaterDto> getTheaterByCity(String city){
        List<Theater> theater = theaterRepo.findByCity(city);
        return theater.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private TheaterDto mapToDto(Theater savetheater) {
        TheaterDto theaterDto = new TheaterDto();
        theaterDto.setId(savetheater.getId());   // ✅ BUG FIX — was missing, caused ID=0
        theaterDto.setTotalScreen(savetheater.getTotalScreen());
        theaterDto.setCity(savetheater.getCity());
        theaterDto.setName(savetheater.getName());
        theaterDto.setAddress(savetheater.getAddress());
        return theaterDto;
    }

    private Theater mapToentity(TheaterDto theaterDto) {
        Theater theater = new Theater();
        theater.setAddress(theaterDto.getAddress());
        theater.setCity(theaterDto.getCity());
        theater.setName(theaterDto.getName());
        theater.setTotalScreen(theaterDto.getTotalScreen());
        return theater;
    }


public void deleteTheater(Long id) {
    Theater theater = theaterRepo.findById(id)
            .orElseThrow(() -> new ResouceNotFoundException("Theater not found with id: " + id));
    theaterRepo.delete(theater);
}
}