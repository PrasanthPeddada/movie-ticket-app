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

    @Query("SELECT DISTINCT m.genre FROM Movie m WHERE m.isActive = true")
    List<String> findAllGenres();

    @Query("SELECT DISTINCT m.language FROM Movie m WHERE m.isActive = true")
    List<String> findAllLanguages();
}