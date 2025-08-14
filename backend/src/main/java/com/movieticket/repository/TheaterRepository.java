package com.movieticket.repository;

import com.movieticket.entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TheaterRepository extends JpaRepository<Theater, Long> {

    List<Theater> findByIsActiveTrue();

    List<Theater> findByCityAndIsActiveTrue(String city);

    List<Theater> findByStateAndIsActiveTrue(String state);

    Theater findByName(String name);

    @Query("SELECT DISTINCT t.city FROM Theater t WHERE t.isActive = true")
    List<String> findAllCities();

    @Query("SELECT DISTINCT t.state FROM Theater t WHERE t.isActive = true")
    List<String> findAllStates();

    @Query("SELECT t FROM Theater t WHERE t.city = :city AND t.state = :state AND t.isActive = true")
    List<Theater> findByCityAndState(@Param("city") String city, @Param("state") String state);
}