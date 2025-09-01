package com.movieticket.repository;

import com.movieticket.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

       

        @Query("SELECT b FROM Booking b " +
                        "LEFT JOIN FETCH b.show s " +
                        "LEFT JOIN FETCH s.movie m " +
                        "LEFT JOIN FETCH s.screen sc " +
                        "LEFT JOIN FETCH sc.theater t " +
                        "WHERE b.user.id = :userId " +
                        "ORDER BY b.bookingDate DESC")
        List<Booking> findBookingsWithDetailsByUserId(@Param("userId") Long userId);

        List<Booking> findByShowId(Long showId);

       

         Booking findByBookingId( String bookingIdLong);
}