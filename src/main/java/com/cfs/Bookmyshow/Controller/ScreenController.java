package com.cfs.Bookmyshow.Controller;

import com.cfs.Bookmyshow.DTO.ScreenDto;
import com.cfs.Bookmyshow.Service.ScreenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/screen")
@CrossOrigin(origins = "*")
public class ScreenController {

    @Autowired
    private ScreenService screenService;

    // POST /api/screen/create
    // Body: { "screenName": "Screen 1", "totalSeats": 100, "theater": { "id": 1 } }
    @PostMapping("/create")
    public ResponseEntity<ScreenDto> createScreen(@RequestBody ScreenDto screenDto) {
        return ResponseEntity.ok(screenService.createScreen(screenDto));
    }

    // GET /api/screen/list
    @GetMapping("/list")
    public ResponseEntity<List<ScreenDto>> getAllScreens() {
        return ResponseEntity.ok(screenService.getAllScreens());
    }

    // GET /api/screen/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ScreenDto> getScreenById(@PathVariable Long id) {
        return ResponseEntity.ok(screenService.getScreenById(id));
    }

    // GET /api/screen/theater/{theaterId}  — get all screens for a theater
    @GetMapping("/theater/{theaterId}")
    public ResponseEntity<List<ScreenDto>> getScreensByTheater(@PathVariable Long theaterId) {
        return ResponseEntity.ok(screenService.getScreensByTheater(theaterId));
    }

    // DELETE /api/screen/delete/{id}
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteScreen(@PathVariable Long id) {
        screenService.deleteScreen(id);
        return ResponseEntity.ok("Screen deleted successfully");
    }
}