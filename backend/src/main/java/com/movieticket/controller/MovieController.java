package com.movieticket.controller;

import com.movieticket.dto.MovieWithShowsResponseDTO;
import com.movieticket.entity.Movie;
import com.movieticket.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/movies")
@CrossOrigin(origins = "*")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> movies = movieService.getAllActiveMovies();
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        Movie movie = movieService.getMovieById(id);
        return ResponseEntity.ok(movie);
    }


    @GetMapping("/{id}/with-shows")
    public ResponseEntity<MovieWithShowsResponseDTO> getMoviewWithShowsResponseDTO(@PathVariable Long id) {
        MovieWithShowsResponseDTO movie = movieService.getMoviewWithShowsResponseDTO(id);
        return ResponseEntity.ok(movie);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Movie>> searchMovies(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) Double minRating) {

        List<Movie> movies = movieService.searchMovies(title, genre, language, minRating);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/genres")
    public ResponseEntity<List<String>> getAllGenres() {
        List<String> genres = movieService.getAllGenres();
        return ResponseEntity.ok(genres);
    }

    @GetMapping("/languages")
    public ResponseEntity<List<String>> getAllLanguages() {
        List<String> languages = movieService.getAllLanguages();
        return ResponseEntity.ok(languages);
    }

    @PostMapping
    public ResponseEntity<?> createMovie(@Valid @RequestBody Movie movie) {
        try {
            Movie createdMovie = movieService.createMovie(movie);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Movie created successfully");
            response.put("movie", createdMovie);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMovie(@PathVariable Long id, @Valid @RequestBody Movie movie) {
        try {
            Movie updatedMovie = movieService.updateMovie(id, movie);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Movie updated successfully");
            response.put("movie", updatedMovie);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable Long id) {
        try {
            movieService.deleteMovie(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Movie deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<Movie>> getUpcomingMovies() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endDate = now.plusMonths(3);
        List<Movie> movies = movieService.getMoviesByReleaseDateRange(now, endDate);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/top-rated")
    public ResponseEntity<List<Movie>> getTopRatedMovies(@RequestParam(defaultValue = "4.0") Double minRating) {
        List<Movie> movies = movieService.getMoviesByRating(minRating);
        return ResponseEntity.ok(movies);
    }
}