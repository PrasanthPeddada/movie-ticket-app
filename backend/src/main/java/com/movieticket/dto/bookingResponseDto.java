package com.movieticket.dto;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.movieticket.entity.Booking;
import com.movieticket.entity.Movie;
import com.movieticket.entity.Screen;
import com.movieticket.entity.Show;
import com.movieticket.entity.Theater;
import com.movieticket.entity.User;

public class bookingResponseDto {
    private Map<String, Object> bookingResponse;
    

    public bookingResponseDto(Booking booking){

         this.bookingResponse = new HashMap<>(); 
        this.bookingResponse.put("id", booking.getId());
               this. bookingResponse.put("bookingId", booking.getBookingId());
               this. bookingResponse.put("totalAmount", booking.getTotalAmount());
                this.bookingResponse.put("status", booking.getStatus());
                this.bookingResponse.put("paymentMethod", booking.getPaymentMethod());
                this.bookingResponse.put("bookingDate", booking.getBookingDate());
                this.bookingResponse.put("createdAt", booking.getCreatedAt());
                this.bookingResponse.put("updatedAt", booking.getUpdatedAt());
                this.bookingResponse.put("bookedSeats", booking.getBookedSeats());

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

                this.bookingResponse.put("show", showResponse);
                this.bookingResponse.put("user", userResponse);
        
    }

    
     public void add(String key, Object value) {
        bookingResponse.put(key, value);
    }

    @JsonAnyGetter
    public Map<String, Object> getBookingResponse() {
        return bookingResponse;
    }
}
