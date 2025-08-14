package com.movieticket.repository;

import com.movieticket.entity.BookedSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookedSeatRepository extends JpaRepository<BookedSeat, Long> {

    List<BookedSeat> findByShowId(Long showId);

    List<BookedSeat> findByShowIdAndStatus(Long showId, BookedSeat.SeatStatus status);

    @Query("SELECT bs FROM BookedSeat bs WHERE bs.show.id = :showId AND bs.rowNumber = :rowNumber AND bs.columnNumber = :columnNumber")
    BookedSeat findByShowAndSeatPosition(@Param("showId") Long showId,
            @Param("rowNumber") Integer rowNumber,
            @Param("columnNumber") Integer columnNumber);

    @Query("SELECT bs FROM BookedSeat bs WHERE bs.show.id = :showId AND bs.status = 'HOLD' AND bs.createdAt < :expiryTime")
    List<BookedSeat> findExpiredHoldSeats(@Param("showId") Long showId, @Param("expiryTime") LocalDateTime expiryTime);

    @Query("SELECT COUNT(bs) FROM BookedSeat bs WHERE bs.show.id = :showId AND bs.status = 'BOOKED'")
    Long countBookedSeatsByShow(@Param("showId") Long showId);

    @Query("SELECT COUNT(bs) FROM BookedSeat bs WHERE bs.show.id = :showId AND bs.status = 'HOLD'")
    Long countHoldSeatsByShow(@Param("showId") Long showId);

    @Query("SELECT bs FROM BookedSeat bs WHERE bs.booking.id = :bookingId")
    List<BookedSeat> findByBookingId(@Param("bookingId") Long bookingId);
}