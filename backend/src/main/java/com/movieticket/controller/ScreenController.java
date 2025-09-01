package com.movieticket.controller;

import com.movieticket.dto.screenDto;
import com.movieticket.entity.Screen;
import com.movieticket.service.ScreenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/screens")
@CrossOrigin(origins = "*")
public class ScreenController {

    @Autowired
    private ScreenService screenService;

    @GetMapping
    public ResponseEntity<List<screenDto>> getAllScreens() {
        List<screenDto> screens = screenService.getAllActiveScreens();
        return ResponseEntity.ok(screens);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Screen> getScreenById(@PathVariable Long id) {
        Screen screen = screenService.getScreenById(id);
        return ResponseEntity.ok(screen);
    }

    @GetMapping("/theater/{theaterId}")
    public ResponseEntity<List<Screen>> getScreensByTheater(@PathVariable Long theaterId) {
        List<Screen> screens = screenService.getScreensByTheater(theaterId);
        return ResponseEntity.ok(screens);
    }

    

    @PostMapping
    public ResponseEntity<?> createScreen(@Valid @RequestBody Screen screen) {
        try {
            Screen createdScreen = screenService.createScreen(screen);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Screen created successfully");
            response.put("screen", createdScreen);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateScreen(@PathVariable Long id, @Valid @RequestBody Screen screen) {
        try {
            Screen updatedScreen = screenService.updateScreen(id, screen);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Screen updated successfully");
            response.put("screen", updatedScreen);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteScreen(@PathVariable Long id) {
        try {
            screenService.deleteScreen(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Screen deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}