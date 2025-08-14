package com.movieticket.repository;

import com.movieticket.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findByIsActiveTrue();

    List<Movie> findByGenreAndIsActiveTrue(String genre);

    List<Movie> findByLanguageAndIsActiveTrue(String language);

    List<Movie> findByTitleContainingIgnoreCaseAndIsActiveTrue(String title);

    @Query("SELECT m FROM Movie m WHERE m.rating >= :minRating AND m.isActive = true ORDER BY m.rating DESC")
    List<Movie> findByRatingGreaterThanEqual(@Param("minRating") Double minRating);

    @Query("SELECT m FROM Movie m WHERE m.releaseDate >= :startDate AND m.releaseDate <= :endDate AND m.isActive = true")
    List<Movie> findByReleaseDateBetween(@Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT DISTINCT m.genre FROM Movie m WHERE m.isActive = true")
    List<String> findAllGenres();

    @Query("SELECT DISTINCT m.language FROM Movie m WHERE m.isActive = true")
    List<String> findAllLanguages();
}