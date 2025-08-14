
package com.movieticket.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import com.movieticket.entity.Booking;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

@Service
public class PaymentService {

    @Value("${razorpay.key_id}")
    private String razorpayKeyId;

    @Value("${razorpay.key_secret}")
    private String razorpayKeySecret;

    
    private RazorpayClient razorpayClient;



   
    @PostConstruct
    public void init() {
        try {
            this.razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
        } catch (RazorpayException e) {
            throw new RuntimeException("Failed to initialize Razorpay client", e);
        }
    }

    public Map<String, Object> createPaymentOrder(Booking booking) {
        try {
            JSONObject options = new JSONObject();
            options.put("amount", booking.getTotalAmount().multiply(new BigDecimal("100")).longValue()); // Convert to
                                                                                                         // paise
            options.put("currency", "INR");
            options.put("receipt", "booking_" + booking.getId());
            options.put("notes", new JSONObject()
                    .put("booking_id", booking.getId().toString())
                    .put("user_email", booking.getUser().getEmail())
                    .put("movie_name", booking.getShow().getMovie().getTitle()));

            Order order = razorpayClient.orders.create(options);

            Map<String, Object> response = new HashMap<>();
            response.put("orderId", order.get("id"));
            response.put("amount", order.get("amount"));
            response.put("currency", order.get("currency"));
            response.put("keyId", razorpayKeyId);
            response.put("bookingId", booking.getId());

            return response;
        } catch (RazorpayException e) {
            throw new RuntimeException("Failed to create payment order", e);
        }
    }

    public boolean verifyPayment(String paymentId, String orderId, String signature) {
        try {
            JSONObject attributes = new JSONObject();
            attributes.put("razorpay_payment_id", paymentId);
            attributes.put("razorpay_order_id", orderId);
            attributes.put("razorpay_signature", signature);

            Utils.verifyPaymentSignature(attributes, razorpayKeySecret);
            return true;
        } catch (RazorpayException e) {
            return false;
        }
    }

    public Map<String, Object> processPaymentSuccess(String paymentId, String orderId, String signature) {
        if (verifyPayment(paymentId, orderId, signature)) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("paymentId", paymentId);
            response.put("orderId", orderId);
            response.put("message", "Payment verified successfully");
            return response;
        } else {
            throw new RuntimeException("Payment verification failed");
        }
    }
}