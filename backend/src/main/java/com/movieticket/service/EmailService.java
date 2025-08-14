package com.movieticket.service;

import com.movieticket.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

   @Autowired
private JavaMailSender mailSender;
   
    @Value("${spring.mail.username}")
private String fromEmail; // Inject from application.properties

public void sendVerificationEmail(User user) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(fromEmail); 
    message.setTo(user.getEmail());
    message.setSubject("Email Verification - Movie Ticket Booking");
    message.setText("Hello " + user.getName() + ",\n\n" +
            "Please click the following link to verify your email address:\n" +
            "http://localhost:4200/verify?token=" + user.getVerificationToken() + "\n\n" +
            "If you didn't create an account, please ignore this email.\n\n" +
            "Best regards,\n Movie Ticket Booking Team");

    mailSender.send(message);
}

    public void sendPasswordResetEmail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(user.getEmail());
        message.setSubject("Password Reset - Movie Ticket Booking");
        message.setText("Hello " + user.getName() + ",\n\n" +
                "You requested a password reset. Please click the following link to reset your password:\n" +
                "http://localhost:4200/reset-password?token=" + user.getResetToken() + "\n\n" +
                "This link will expire in 24 hours.\n\n" +
                "If you didn't request a password reset, please ignore this email.\n\n" +
                "Best regards,\nMovie Ticket Booking Team");

        mailSender.send(message);
    }

    public void sendBookingConfirmationEmail(User user, String bookingId, String movieTitle, String showTime) {
       if (user == null || user.getEmail() == null) {
        throw new IllegalArgumentException("User or email must not be null");
    }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(user.getEmail());
        message.setSubject("Booking Confirmation - Movie Ticket Booking");
        message.setText("Hello " + user.getName() + ",\n\n" +
                "Your booking has been confirmed!\n\n" +
                "Booking ID: " + bookingId + "\n" +
                "Movie: " + movieTitle + "\n" +
                "Show Time: " + showTime + "\n\n" +
                "Please arrive at the theater 15 minutes before the show time.\n\n" +
                "Thank you for choosing Movie Ticket Booking!\n\n" +
                "Best regards,\nMovie Ticket Booking Team");

        mailSender.send(message);
    }
}