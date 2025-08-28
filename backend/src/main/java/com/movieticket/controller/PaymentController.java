package com.movieticket.controller;

import com.movieticket.entity.Booking;
import com.movieticket.service.BookingService;
import com.movieticket.service.EmailService;
import com.movieticket.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/create-order/{bookingId}")
    public ResponseEntity<?> createPaymentOrder(@PathVariable Long bookingId) {
        try {
            Booking booking = bookingService.getBookingById(bookingId);
            if (booking == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Booking not found");
                return ResponseEntity.status(404).body(error);
            }

            Map<String, Object> paymentOrder = paymentService.createPaymentOrder(booking);
            return ResponseEntity.ok(paymentOrder);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to create payment order: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody Map<String, String> paymentData) {
        try {
            String paymentId = paymentData.get("razorpay_payment_id");
            String orderId = paymentData.get("razorpay_order_id");
            String signature = paymentData.get("razorpay_signature");
            String bookingId = paymentData.get("booking_id");

            if (paymentId == null || orderId == null || signature == null || bookingId == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Missing payment verification data");
                return ResponseEntity.badRequest().body(error);
            }

            // Verify payment signature
            boolean isVerified = paymentService.verifyPayment(paymentId, orderId, signature);

            if (isVerified) {
                // Update booking status
                Booking booking = bookingService.getBookingById(Long.valueOf(bookingId));
                booking.setStatus(Booking.BookingStatus.CONFIRMED);
                booking.setPaymentId(paymentId);
                booking.setTransactionId(orderId);
                bookingService.updateBooking(booking);

                emailService.sendBookingConfirmationEmail(booking.getUser(), bookingId,  booking.getShow().getMovie().getTitle(),
                booking.getShow().getShowTime().toString());

                


                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Payment verified successfully");
                response.put("bookingId", bookingId);
                response.put("paymentId", paymentId);
                response.put("orderId", orderId);
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Payment verification failed");
                return ResponseEntity.badRequest().body(error);
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Payment verification error: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<?> handleWebhook(@RequestBody String payload,
            @RequestHeader("X-Razorpay-Signature") String signature) {
        try {
        

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Webhook received successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Webhook processing error: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
}