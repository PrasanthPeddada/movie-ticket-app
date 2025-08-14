package com.movieticket.repository;

import com.movieticket.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {

    List<Show> findByMovieIdAndIsActiveTrue(Long movieId);

    List<Show> findByScreenIdAndIsActiveTrue(Long screenId);

    @Query("SELECT s FROM Show s WHERE s.screen.theater.id = :theaterId AND s.isActive = true")
    List<Show> findByTheaterIdAndIsActiveTrue(@Param("theaterId") Long theaterId);

    @Query("SELECT s FROM Show s WHERE s.movie.id = :movieId AND s.showTime >= :startTime AND s.isActive = true ORDER BY s.showTime")
    List<Show> findUpcomingShowsByMovie(@Param("movieId") Long movieId, @Param("startTime") LocalDateTime startTime);

    @Query("SELECT s FROM Show s WHERE s.screen.theater.id = :theaterId AND s.showTime >= :startTime AND s.isActive = true ORDER BY s.showTime")
    List<Show> findUpcomingShowsByTheater(@Param("theaterId") Long theaterId,
            @Param("startTime") LocalDateTime startTime);

    @Query("SELECT s FROM Show s WHERE s.showTime >= :startTime AND s.showTime <= :endTime AND s.isActive = true ORDER BY s.showTime")
    List<Show> findShowsByDateRange(@Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    @Query("SELECT s FROM Show s WHERE s.screen.id = :screenId AND s.showTime >= :startTime AND s.showTime <= :endTime AND s.isActive = true")
    List<Show> findShowsByScreenAndDateRange(@Param("screenId") Long screenId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
}