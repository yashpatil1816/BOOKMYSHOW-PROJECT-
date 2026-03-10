package com.cfs.Bookmyshow.Controller;

import com.cfs.Bookmyshow.DTO.ShowDto;
import com.cfs.Bookmyshow.Service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/show")
@CrossOrigin(origins = "*")
public class ShowController {

    @Autowired private ShowService showService;

    // POST /api/show/create
    @PostMapping("/create")
    public ResponseEntity<ShowDto> createShow(@RequestBody ShowDto showDto) {
        return new ResponseEntity<>(showService.createShow(showDto), HttpStatus.CREATED);
    }

    // GET /api/show/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ShowDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(showService.getShowById(id));
    }

    // GET /api/show/list
    @GetMapping("/list")
    public ResponseEntity<List<ShowDto>> getAll() {
        return ResponseEntity.ok(showService.getAllShows());
    }
}