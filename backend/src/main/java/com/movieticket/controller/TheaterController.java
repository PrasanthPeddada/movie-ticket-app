package com.movieticket.controller;


import com.movieticket.entity.Theater;

import com.movieticket.service.TheaterService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/theaters")
@CrossOrigin(origins = "*")
public class TheaterController {

    @Autowired
    private TheaterService theaterService;

   

    @GetMapping
    public ResponseEntity<List<Theater>> getAllTheaters() {
        List<Theater> theaters = theaterService.getAllActiveTheaters();
        return ResponseEntity.ok(theaters);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Theater> getTheaterById(@PathVariable Long id) {
        Theater theater = theaterService.getTheaterById(id);
        return ResponseEntity.ok(theater);
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<Theater>> getTheatersByCity(@PathVariable String city) {
        List<Theater> theaters = theaterService.getTheatersByCity(city);
        return ResponseEntity.ok(theaters);
    }

    @GetMapping("/state/{state}")
    public ResponseEntity<List<Theater>> getTheatersByState(@PathVariable String state) {
        List<Theater> theaters = theaterService.getTheatersByState(state);
        return ResponseEntity.ok(theaters);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Theater>> searchTheaters(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String state) {

        return ResponseEntity.ok(theaterService.searchTheaters(city,state));
    }

    @GetMapping("/cities")
    public ResponseEntity<List<String>> getAllCities() {
        List<String> cities = theaterService.getAllCities();
        return ResponseEntity.ok(cities);
    }

    @GetMapping("/states")
    public ResponseEntity<List<String>> getAllStates() {
        List<String> states = theaterService.getAllStates();
        return ResponseEntity.ok(states);
    }

    @PostMapping
    public ResponseEntity<?> createTheater(@Valid @RequestBody Theater theater) {
        try {
        Theater createdTheater = theaterService.createTheater(theater);  
        
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Theater created successfully");
            response.put("theater", createdTheater);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTheater(@PathVariable Long id, @Valid @RequestBody Theater theater) {
        try {
            Theater updatedTheater = theaterService.updateTheater(id, theater);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Theater updated successfully");
            response.put("theater", updatedTheater);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTheater(@PathVariable Long id) {
        try {
            theaterService.deleteTheater(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Theater deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}