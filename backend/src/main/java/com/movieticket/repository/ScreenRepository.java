package com.movieticket.repository;

import com.movieticket.entity.Screen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScreenRepository extends JpaRepository<Screen, Long> {

    List<Screen> findByTheaterIdAndIsActiveTrue(Long theaterId);

    List<Screen> findByTheaterId(Long theaterId);

    @Query("SELECT s FROM Screen s WHERE s.theater.id = :theaterId AND s.isActive = true")
    List<Screen> findActiveScreensByTheater(@Param("theaterId") Long theaterId);

    @Query("SELECT COUNT(s) FROM Screen s WHERE s.theater.id = :theaterId AND s.isActive = true")
    Long countActiveScreensByTheater(@Param("theaterId") Long theaterId);
}