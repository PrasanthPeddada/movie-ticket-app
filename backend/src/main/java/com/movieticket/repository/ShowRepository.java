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


    @Query("SELECT s FROM Show s WHERE s.screen.id = :screenId AND s.showTime >= :startTime AND s.showTime <= :endTime AND s.isActive = true")
    List<Show> findShowsByScreenAndDateRange(@Param("screenId") Long screenId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
}