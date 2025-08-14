package com.movieticket.controller;

import com.movieticket.entity.Show;
import com.movieticket.entity.BookedSeat;
import com.movieticket.service.ShowService;
import com.movieticket.repository.BookedSeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/shows")
@CrossOrigin(origins = "*")
public class ShowController {

    @Autowired
    private ShowService showService;

    @Autowired
    private BookedSeatRepository bookedSeatRepository;

    @GetMapping("/{showId}/seats")
    public ResponseEntity<Map<String, Object>> getSeatLayoutForShow(@PathVariable Long showId) {
        Show show = showService.getShowById(showId);
        int totalRows = show.getScreen().getTotalRows();
        int totalColumns = show.getScreen().getTotalColumns();
        List<BookedSeat> seats = bookedSeatRepository.findByShowId(showId); // get all seats for the show
        Map<String, Object> response = new HashMap<>();
        response.put("totalRows", totalRows);
        response.put("totalColumns", totalColumns);
        response.put("seats", seats);
        response.put("goldSeatPrice", show.getGoldSeatPrice());
        response.put("silverSeatPrice", show.getSilverSeatPrice());
        response.put("vipSeatPrice", show.getVipSeatPrice());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<Show>> getAllShows() {
        List<Show> shows = showService.getAllActiveShows();
        return ResponseEntity.ok(shows);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Show> getShowById(@PathVariable Long id) {
        Show show = showService.getShowById(id);
        return ResponseEntity.ok(show);
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<Show>> getShowsByMovie(@PathVariable Long movieId) {
        List<Show> shows = showService.getShowsByMovie(movieId);
        return ResponseEntity.ok(shows);
    }

    @GetMapping("/movie/{movieId}/upcoming")
    public ResponseEntity<List<Show>> getUpcomingShowsByMovie(@PathVariable Long movieId) {
        List<Show> shows = showService.getUpcomingShowsByMovie(movieId);
        return ResponseEntity.ok(shows);
    }

    @GetMapping("/screen/{screenId}")
    public ResponseEntity<List<Show>> getShowsByScreen(@PathVariable Long screenId) {
        List<Show> shows = showService.getShowsByScreen(screenId);
        return ResponseEntity.ok(shows);
    }

    @GetMapping("/theater/{theaterId}")
    public ResponseEntity<List<Show>> getShowsByTheater(@PathVariable Long theaterId) {
        List<Show> shows = showService.getShowsByTheater(theaterId);
        return ResponseEntity.ok(shows);
    }

    @GetMapping("/theater/{theaterId}/upcoming")
    public ResponseEntity<List<Show>> getUpcomingShowsByTheater(@PathVariable Long theaterId) {
        List<Show> shows = showService.getUpcomingShowsByTheater(theaterId);
        return ResponseEntity.ok(shows);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Show>> getShowsByDateRange(
            @RequestParam String startTime,
            @RequestParam String endTime) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(startTime, formatter);
        LocalDateTime end = LocalDateTime.parse(endTime, formatter);

        List<Show> shows = showService.getShowsByDateRange(start, end);
        return ResponseEntity.ok(shows);
    }

    @PostMapping
    public ResponseEntity<?> createShow(@Valid @RequestBody Show show) {
        try {
            // Check if show time is available
            if (!showService.isShowTimeAvailable(show.getScreen().getId(), show.getShowTime(), null)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Show time conflicts with existing shows");
                return ResponseEntity.badRequest().body(error);
            }

            Show createdShow = showService.createShow(show);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Show created successfully");
            response.put("show", createdShow);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateShow(@PathVariable Long id, @Valid @RequestBody Show show) {
        try {
            // Check if show time is available (excluding current show)
            if (!showService.isShowTimeAvailable(show.getScreen().getId(), show.getShowTime(), id)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Show time conflicts with existing shows");
                return ResponseEntity.badRequest().body(error);
            }

            Show updatedShow = showService.updateShow(id, show);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Show updated successfully");
            response.put("show", updatedShow);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteShow(@PathVariable Long id) {
        try {
            showService.deleteShow(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Show deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}