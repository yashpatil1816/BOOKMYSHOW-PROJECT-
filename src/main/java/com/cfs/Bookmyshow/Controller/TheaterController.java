package com.cfs.Bookmyshow.Controller;

import com.cfs.Bookmyshow.DTO.TheaterDto;
import com.cfs.Bookmyshow.Service.TheatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/theater")
@CrossOrigin(origins = "*")
public class TheaterController {

    @Autowired private TheatherService theatherService;

    // POST /api/theater/create
    @PostMapping("/create")
    public ResponseEntity<TheaterDto> createTheater(@RequestBody TheaterDto theaterDto) {
        return new ResponseEntity<>(theatherService.craeteTheater(theaterDto), HttpStatus.CREATED);
    }

    // GET /api/theater/{id}
    @GetMapping("/{id}")
    public ResponseEntity<TheaterDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(theatherService.getTheaterById(id));
    }

    // GET /api/theater/list
    @GetMapping("/list")
    public ResponseEntity<List<TheaterDto>> getAll() {
        return ResponseEntity.ok(theatherService.getALLTheater());
    }

    // GET /api/theater/city/{city}
    @GetMapping("/city/{city}")
    public ResponseEntity<List<TheaterDto>> getByCity(@PathVariable String city) {
        return ResponseEntity.ok(theatherService.getTheaterByCity(city));
    }

    // DELETE /api/theater/delete/{id}  ← admin uses this
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTheater(@PathVariable Long id) {
        theatherService.deleteTheater(id);
        return ResponseEntity.ok("Theater deleted");
    }
}