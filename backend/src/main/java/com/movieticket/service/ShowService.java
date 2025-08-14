package com.movieticket.service;

import com.movieticket.entity.Show;
import com.movieticket.entity.Movie;
import com.movieticket.entity.Screen;
import com.movieticket.repository.ShowRepository;
import com.movieticket.repository.MovieRepository;
import com.movieticket.repository.ScreenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShowService {

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ScreenRepository screenRepository;

    public List<Show> getAllActiveShows() {
        return showRepository.findAll().stream()
                .filter(Show::isActive)
                .collect(java.util.stream.Collectors.toList());
    }

    public Show getShowById(Long id) {
        return showRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Show not found"));
    }

    public List<Show> getShowsByMovie(Long movieId) {
        return showRepository.findByMovieIdAndIsActiveTrue(movieId);
    }

    public List<Show> getShowsByScreen(Long screenId) {
        return showRepository.findByScreenIdAndIsActiveTrue(screenId);
    }

    public List<Show> getShowsByTheater(Long theaterId) {
        return showRepository.findByTheaterIdAndIsActiveTrue(theaterId);
    }

    public List<Show> getUpcomingShowsByMovie(Long movieId) {
        return showRepository.findUpcomingShowsByMovie(movieId, LocalDateTime.now());
    }

    public List<Show> getUpcomingShowsByTheater(Long theaterId) {
        return showRepository.findUpcomingShowsByTheater(theaterId, LocalDateTime.now());
    }

    public List<Show> getShowsByDateRange(LocalDateTime startTime, LocalDateTime endTime) {
        return showRepository.findShowsByDateRange(startTime, endTime);
    }

    public Show createShow(Show show) {
        // Verify movie exists
        Movie movie = movieRepository.findById(show.getMovie().getId())
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        // Verify screen exists
        Screen screen = screenRepository.findById(show.getScreen().getId())
                .orElseThrow(() -> new RuntimeException("Screen not found"));

        show.setMovie(movie);
        show.setScreen(screen);
        show.setActive(true);

        return showRepository.save(show);
    }

    public Show updateShow(Long id, Show showDetails) {
        Show show = getShowById(id);

        show.setShowTime(showDetails.getShowTime());
        show.setGoldSeatPrice(showDetails.getGoldSeatPrice());
        show.setSilverSeatPrice(showDetails.getSilverSeatPrice());
        show.setVipSeatPrice(showDetails.getVipSeatPrice());
        show.setUpdatedAt(LocalDateTime.now());

        return showRepository.save(show);
    }

    public void deleteShow(Long id) {
        Show show = getShowById(id);
        show.setActive(false);
        show.setUpdatedAt(LocalDateTime.now());
        showRepository.save(show);
    }

    public boolean isShowTimeAvailable(Long screenId, LocalDateTime showTime, Long excludeShowId) {
        // Check if there are any conflicting shows for the same screen
        LocalDateTime showEndTime = showTime.plusMinutes(180); // Assuming 3 hours per show

        List<Show> conflictingShows = showRepository.findShowsByScreenAndDateRange(screenId, showTime, showEndTime);

        if (excludeShowId != null) {
            conflictingShows = conflictingShows.stream()
                    .filter(s -> !s.getId().equals(excludeShowId))
                    .collect(java.util.stream.Collectors.toList());
        }

        return conflictingShows.isEmpty();
    }
}