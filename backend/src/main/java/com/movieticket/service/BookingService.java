package com.movieticket.service;

import com.movieticket.entity.Booking;
import com.movieticket.entity.Movie;
import com.movieticket.entity.Screen;
import com.movieticket.entity.Show;
import com.movieticket.entity.Theater;
import com.movieticket.entity.BookedSeat;
import com.movieticket.entity.User;
import com.movieticket.repository.BookingRepository;
import com.movieticket.repository.BookedSeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookingService {

   
 @Autowired
    private UserService userService;
    @Autowired
    private ShowService showService;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private BookedSeatRepository bookedSeatRepository;
    @Autowired
    private EmailService emailService;

    public Map<String, Object> processBooking(String email, Map<String, Object> bookingRequest) {
        User user = userService.findByEmail(email);
        if (user == null) {
            throw new IllegalStateException("User not found for email: " + email);
        }

        Long showId = Long.valueOf(bookingRequest.get("showId").toString());
        List<Map<String, Object>> selectedSeats = (List<Map<String, Object>>) bookingRequest.get("selectedSeats");
        BigDecimal totalAmount = new BigDecimal(bookingRequest.get("totalAmount").toString());

        Show show = showService.getShowById(showId);
        if (show == null) {
            throw new IllegalStateException("Show not found for ID: " + showId);
        }

        // Check seat availability
        for (Map<String, Object> seatData : selectedSeats) {
            int row = (Integer) seatData.get("row");
            int col = (Integer) seatData.get("col");
            if (!isSeatAvailable(showId, row, col)) {
                throw new IllegalStateException(
                    "Seat at row " + row + ", column " + col + " is already booked."
                );
            }
        }

        // Create booking
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setShow(show);
        booking.setTotalAmount(totalAmount);
        booking.setStatus(Booking.BookingStatus.PENDING);
        booking.setBookingId("BK" + System.currentTimeMillis());
        booking.setBookingDate(LocalDateTime.now());
        booking.setCreatedAt(LocalDateTime.now());
        booking.setUpdatedAt(LocalDateTime.now());

        Booking createdBooking = bookingRepository.save(booking);

        // Create booked seats
        for (Map<String, Object> seatData : selectedSeats) {
            int row = (Integer) seatData.get("row");
            int col = (Integer) seatData.get("col");

            BookedSeat bookedSeat = new BookedSeat();
            bookedSeat.setBooking(createdBooking);
            bookedSeat.setShow(show);
            bookedSeat.setRowNumber(row);
            bookedSeat.setColumnNumber(col);
            bookedSeat.setStatus(BookedSeat.SeatStatus.BOOKED);

            String category = getSeatCategory(row, show.getScreen().getTotalRows());
            BigDecimal price = getSeatPrice(category, show);
            bookedSeat.setPrice(price);

            bookedSeatRepository.save(bookedSeat);
        }


        // Prepare response
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Booking created successfully");
        response.put("booking", createdBooking);
        response.put("bookingId", createdBooking.getId());

        return response;
    }

    

    private String getSeatCategory(int row, int totalRows) {
        if (row <= totalRows / 3) return "VIP";
        else if (row <= (totalRows * 2) / 3) return "GOLD";
        else return "SILVER";
    }

    private BigDecimal getSeatPrice(String category, Show show) {
        switch (category) {
            case "VIP": return show.getVipSeatPrice();
            case "GOLD": return show.getGoldSeatPrice();
            default: return show.getSilverSeatPrice();
        }
    }
   

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    public Booking updateBooking(Booking booking) {
        booking.setUpdatedAt(LocalDateTime.now());
        return bookingRepository.save(booking);
    }

    public void cancelBooking(Long id) {
        Booking booking = getBookingById(id);
        booking.setStatus(Booking.BookingStatus.CANCELLED);
        booking.setUpdatedAt(LocalDateTime.now());
        bookingRepository.save(booking);
    }

    public List<Map<String,Object>> getBookingsByUser(Long userId) {
        List<Booking> bookings= bookingRepository.findBookingsWithDetailsByUserId(userId);
        List<Map<String, Object>> bookingResponses = new ArrayList<>();
            for (Booking booking : bookings) {
                Map<String, Object> bookingResponse = new HashMap<>();
                bookingResponse.put("id", booking.getId());
                bookingResponse.put("bookingId", booking.getBookingId());
                bookingResponse.put("totalAmount", booking.getTotalAmount());
                bookingResponse.put("status", booking.getStatus());
                bookingResponse.put("paymentMethod", booking.getPaymentMethod());
                bookingResponse.put("bookingDate", booking.getBookingDate());
                bookingResponse.put("createdAt", booking.getCreatedAt());
                bookingResponse.put("updatedAt", booking.getUpdatedAt());
                bookingResponse.put("bookedSeats", booking.getBookedSeats());

                // Add show with movie information
                Show show = booking.getShow();
                Map<String, Object> showResponse = new HashMap<>();
                showResponse.put("id", show.getId());
                showResponse.put("showTime", show.getShowTime());
                showResponse.put("goldSeatPrice", show.getGoldSeatPrice());
                showResponse.put("silverSeatPrice", show.getSilverSeatPrice());
                showResponse.put("vipSeatPrice", show.getVipSeatPrice());
                showResponse.put("createdAt", show.getCreatedAt());
                showResponse.put("updatedAt", show.getUpdatedAt());
                showResponse.put("active", show.isActive());

                // Add movie information
                Movie movie = show.getMovie();
                Map<String, Object> movieResponse = new HashMap<>();
                movieResponse.put("id", movie.getId());
                movieResponse.put("title", movie.getTitle());
                movieResponse.put("description", movie.getDescription());
                movieResponse.put("genre", movie.getGenre());
                movieResponse.put("language", movie.getLanguage());
                movieResponse.put("durationMinutes", movie.getDurationMinutes());
                movieResponse.put("posterUrl", movie.getPosterUrl());
                movieResponse.put("trailerUrl", movie.getTrailerUrl());
                movieResponse.put("rating", movie.getRating());
                movieResponse.put("releaseDate", movie.getReleaseDate());
                movieResponse.put("isActive", movie.isActive());
                movieResponse.put("createdAt", movie.getCreatedAt());
                movieResponse.put("updatedAt", movie.getUpdatedAt());

                showResponse.put("movie", movieResponse);

                // Add screen and theater information
                Screen screen = show.getScreen();
                Map<String, Object> screenResponse = new HashMap<>();
                screenResponse.put("id", screen.getId());
                screenResponse.put("name", screen.getName());
                screenResponse.put("totalRows", screen.getTotalRows());
                screenResponse.put("totalColumns", screen.getTotalColumns());
                screenResponse.put("createdAt", screen.getCreatedAt());
                screenResponse.put("updatedAt", screen.getUpdatedAt());
                screenResponse.put("active", screen.isActive());

                Theater theater = screen.getTheater();
                Map<String, Object> theaterResponse = new HashMap<>();
                theaterResponse.put("id", theater.getId());
                theaterResponse.put("name", theater.getName());
                theaterResponse.put("address", theater.getAddress());
                theaterResponse.put("city", theater.getCity());
                theaterResponse.put("state", theater.getState());
                theaterResponse.put("pincode", theater.getPincode());
                theaterResponse.put("phoneNumber", theater.getPhoneNumber());
                theaterResponse.put("createdAt", theater.getCreatedAt());
                theaterResponse.put("updatedAt", theater.getUpdatedAt());
                theaterResponse.put("active", theater.isActive());

                screenResponse.put("theater", theaterResponse);
                showResponse.put("screen", screenResponse);

                bookingResponse.put("show", showResponse);
                bookingResponses.add(bookingResponse);
            }
            return bookingResponses;
    }

    public List<Map<String,Object>> getAllBookings(){

        List<Booking> bookings= bookingRepository.findAll();
        List<Map<String, Object>> bookingResponses = new ArrayList<>();
            for (Booking booking : bookings) {
                Map<String, Object> bookingResponse = new HashMap<>();
                bookingResponse.put("id", booking.getId());
                bookingResponse.put("bookingId", booking.getBookingId());
                bookingResponse.put("totalAmount", booking.getTotalAmount());
                bookingResponse.put("status", booking.getStatus());
                bookingResponse.put("paymentMethod", booking.getPaymentMethod());
                bookingResponse.put("bookingDate", booking.getBookingDate());
                bookingResponse.put("createdAt", booking.getCreatedAt());
                bookingResponse.put("updatedAt", booking.getUpdatedAt());
                bookingResponse.put("bookedSeats", booking.getBookedSeats());

                // Add show with movie information
                Show show = booking.getShow();
                Map<String, Object> showResponse = new HashMap<>();
                showResponse.put("id", show.getId());
                showResponse.put("showTime", show.getShowTime());
                showResponse.put("goldSeatPrice", show.getGoldSeatPrice());
                showResponse.put("silverSeatPrice", show.getSilverSeatPrice());
                showResponse.put("vipSeatPrice", show.getVipSeatPrice());
                showResponse.put("createdAt", show.getCreatedAt());
                showResponse.put("updatedAt", show.getUpdatedAt());
                showResponse.put("active", show.isActive());

                User user = booking.getUser();
                Map<String, Object> userResponse = new HashMap<>();
                userResponse.put("id", user.getId());   
                userResponse.put("name", user.getName());
                userResponse.put("email", user.getEmail());
                userResponse.put("phoneNumber", user.getPhoneNumber());
                

                // Add movie information
                Movie movie = show.getMovie();
                Map<String, Object> movieResponse = new HashMap<>();
                movieResponse.put("id", movie.getId());
                movieResponse.put("title", movie.getTitle());
                movieResponse.put("description", movie.getDescription());
                movieResponse.put("genre", movie.getGenre());
                movieResponse.put("language", movie.getLanguage());
                movieResponse.put("durationMinutes", movie.getDurationMinutes());
                movieResponse.put("posterUrl", movie.getPosterUrl());
                movieResponse.put("trailerUrl", movie.getTrailerUrl());
                movieResponse.put("rating", movie.getRating());
                movieResponse.put("releaseDate", movie.getReleaseDate());
                movieResponse.put("isActive", movie.isActive());
                movieResponse.put("createdAt", movie.getCreatedAt());
                movieResponse.put("updatedAt", movie.getUpdatedAt());

                showResponse.put("movie", movieResponse);

                // Add screen and theater information
                Screen screen = show.getScreen();
                Map<String, Object> screenResponse = new HashMap<>();
                screenResponse.put("id", screen.getId());
                screenResponse.put("name", screen.getName());
                screenResponse.put("totalRows", screen.getTotalRows());
                screenResponse.put("totalColumns", screen.getTotalColumns());
                screenResponse.put("createdAt", screen.getCreatedAt());
                screenResponse.put("updatedAt", screen.getUpdatedAt());
                screenResponse.put("active", screen.isActive());

                Theater theater = screen.getTheater();
                Map<String, Object> theaterResponse = new HashMap<>();
                theaterResponse.put("id", theater.getId());
                theaterResponse.put("name", theater.getName());
                theaterResponse.put("address", theater.getAddress());
                theaterResponse.put("city", theater.getCity());
                theaterResponse.put("state", theater.getState());
                theaterResponse.put("pincode", theater.getPincode());
                theaterResponse.put("phoneNumber", theater.getPhoneNumber());
                theaterResponse.put("createdAt", theater.getCreatedAt());
                theaterResponse.put("updatedAt", theater.getUpdatedAt());
                theaterResponse.put("active", theater.isActive());

                screenResponse.put("theater", theaterResponse);
                showResponse.put("screen", screenResponse);

                bookingResponse.put("show", showResponse);
                bookingResponse.put("user", userResponse);
        bookingResponses.add(bookingResponse);
    }
    return bookingResponses;

    }

    public BookedSeat createBookedSeat(BookedSeat bookedSeat) {
        bookedSeat.setCreatedAt(LocalDateTime.now());
        bookedSeat.setUpdatedAt(LocalDateTime.now());
        return bookedSeatRepository.save(bookedSeat);
    }

    public List<BookedSeat> getBookedSeatsByBooking(Long bookingId) {
        return bookedSeatRepository.findByBookingId(bookingId);
    }

    public boolean isSeatAvailable(Long showId, int row, int col) {
        System.out.println("Checking seat availability for show: " + showId + ", row: " + row + ", col: " + col);
        BookedSeat existingSeat = bookedSeatRepository.findByShowAndSeatPosition(showId, row, col);
        boolean isAvailable = existingSeat == null || existingSeat.getStatus() != BookedSeat.SeatStatus.BOOKED;
        System.out.println("Seat availability result: " + isAvailable + " (existing seat: " + (existingSeat != null ? existingSeat.getStatus() : "null") + ")");
        return isAvailable;
    }
}