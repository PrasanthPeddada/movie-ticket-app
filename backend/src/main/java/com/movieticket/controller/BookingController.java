package com.movieticket.controller;

import com.movieticket.entity.Booking;
import com.movieticket.entity.BookedSeat;
import com.movieticket.entity.Show;
import com.movieticket.entity.User;
import com.movieticket.service.BookingService;
import com.movieticket.service.ShowService;
import com.movieticket.service.UserService;
import com.movieticket.service.JwtService;
import com.movieticket.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import com.movieticket.entity.Movie;
import com.movieticket.entity.Screen;
import com.movieticket.entity.Theater;

@RestController
@RequestMapping("/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ShowService showService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private EmailService emailService;

   
    @PostMapping
@Transactional
public ResponseEntity<?> createBooking(
        @RequestHeader("Authorization") String token,
        @RequestBody Map<String, Object> bookingRequest) {
    try {
        String email = jwtService.extractEmail(token.replace("Bearer ", ""));
        Map<String, Object> response = bookingService.processBooking(email, bookingRequest);
        return ResponseEntity.ok(response);
    } catch (IllegalStateException e) {
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
    }
}


    @PostMapping("/{bookingId}/payment")
    public ResponseEntity<?> processPayment(@PathVariable Long bookingId,
            @RequestBody Map<String, String> paymentRequest) {
                 
        try {
            System.out.println("Processing payment for booking ID: " + bookingId);
            System.out.println("Payment request: " + paymentRequest);

            String paymentMethod = paymentRequest.get("paymentMethod");
            System.out.println("Payment method: " + paymentMethod);

            Booking booking = bookingService.getBookingById(bookingId);
                
            if (booking == null) {
                System.err.println("Booking not found for ID: " + bookingId);
                Map<String, String> error = new HashMap<>();
                error.put("error", "Booking not found.");
                return ResponseEntity.status(404).body(error);
            }
            

            

            // For Razorpay integration, we'll create a payment order
            // The actual payment will be processed on the frontend
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Payment order created successfully");
            response.put("bookingId", booking.getId());
            response.put("status", "PENDING");
            response.put("paymentMethod", paymentMethod);
            response.put("totalAmount", booking.getTotalAmount());
            response.put("requiresPaymentGateway", true);
           
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error processing payment: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Internal server error: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserBookings(@RequestHeader("Authorization") String token) {
        try {
            String email = jwtService.extractEmail(token.replace("Bearer ", ""));
            User user = userService.findByEmail(email);
            
             return ResponseEntity.ok(bookingService.getBookingsByUser(user.getId()));

            // Create custom response with movie information
           
            }

           
        catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllBookings() {
        try {
            
            return ResponseEntity.ok(bookingService.getAllBookings());
            

            
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookingById(@PathVariable Long id) {
        try {
            Booking booking = bookingService.getBookingById(id);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id) {
        try {
            bookingService.cancelBooking(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Booking cancelled successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

}