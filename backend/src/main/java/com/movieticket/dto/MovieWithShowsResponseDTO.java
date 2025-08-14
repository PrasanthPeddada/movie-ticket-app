package com.movieticket.dto;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.List;
import com.movieticket.dto.ShowResponseDTO;

import com.movieticket.entity.Movie;

public class MovieWithShowsResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String genre;
    private String language;
    private int durationMinutes;
    private String posterUrl;
    private String trailerUrl;
    private double rating;
    private LocalDateTime releaseDate;
    private List<ShowResponseDTO> shows;
    private boolean active;

    public MovieWithShowsResponseDTO(Movie movie) {
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.description = movie.getDescription();
        this.genre = movie.getGenre();
        this.language = movie.getLanguage();
        this.durationMinutes = movie.getDurationMinutes();
        this.posterUrl = movie.getPosterUrl();
        this.trailerUrl = movie.getTrailerUrl();
        this.rating = movie.getRating();
        this.releaseDate = movie.getReleaseDate();
        this.active = movie.isActive();
        this.shows = movie.getShows().stream()
                          .map(ShowResponseDTO::new)
                          .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public LocalDateTime getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDateTime releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<ShowResponseDTO> getShows() {
        return shows;
    }

    public void setShows(List<ShowResponseDTO> shows) {
        this.shows = shows;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
