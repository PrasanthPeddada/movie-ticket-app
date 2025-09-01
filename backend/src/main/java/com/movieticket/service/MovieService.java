package com.movieticket.service;

import com.movieticket.dto.MovieWithShowsResponseDTO;
import com.movieticket.entity.Movie;
import com.movieticket.repository.MovieRepository;
import com.movieticket.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public List<Movie> getAllActiveMovies() {

        return movieRepository.findByIsActiveTrue();
    }

    public MovieWithShowsResponseDTO getMoviewWithShowsResponseDTO(Long id) {
        Movie movie= movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));
        MovieWithShowsResponseDTO movieWithShowsResponseDTO = new MovieWithShowsResponseDTO(movie);
        return movieWithShowsResponseDTO;
    }

    public Movie getMovieById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));
    }

    public List<Movie> searchMovies(String title, String genre, String language, Double minRating) {
        List<Movie> movies = getAllActiveMovies();

        if (title != null && !title.trim().isEmpty()) {
            movies = movies.stream()
                    .filter(movie -> movie.getTitle().toLowerCase().contains(title.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (genre != null && !genre.trim().isEmpty()) {
            movies = movies.stream()
                    .filter(movie -> movie.getGenre().equalsIgnoreCase(genre))
                    .collect(Collectors.toList());
        }

        if (language != null && !language.trim().isEmpty()) {
            movies = movies.stream()
                    .filter(movie -> movie.getLanguage().equalsIgnoreCase(language))
                    .collect(Collectors.toList());
        }

        if (minRating != null) {
            movies = movies.stream()
                    .filter(movie -> movie.getRating() != null && movie.getRating() >= minRating)
                    .collect(Collectors.toList());
        }

        return movies;
    }

    public List<String> getAllGenres() {
        return movieRepository.findAllGenres();
    }

    public List<String> getAllLanguages() {
        return movieRepository.findAllLanguages();
    }

    public Movie createMovie(Movie movie) {
        movie.setActive(true);
        return movieRepository.save(movie);
    }

    public Movie updateMovie(Long id, Movie movieDetails) {
        Movie movie = getMovieById(id);

        movie.setTitle(movieDetails.getTitle());
        movie.setDescription(movieDetails.getDescription());
        movie.setGenre(movieDetails.getGenre());
        movie.setLanguage(movieDetails.getLanguage());
        movie.setDurationMinutes(movieDetails.getDurationMinutes());
        movie.setPosterUrl(movieDetails.getPosterUrl());
        movie.setTrailerUrl(movieDetails.getTrailerUrl());
        movie.setRating(movieDetails.getRating());
        movie.setReleaseDate(movieDetails.getReleaseDate());
        movie.setUpdatedAt(LocalDateTime.now());

        return movieRepository.save(movie);
    }

    public void deleteMovie(Long id) {
        Movie movie = getMovieById(id);
        movie.setActive(false);
        movie.setUpdatedAt(LocalDateTime.now());
        movieRepository.save(movie);
    }

    
}