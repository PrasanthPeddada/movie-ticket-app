package com.movieticket.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "shows")
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    @JsonBackReference(value = "movie-shows")
    private Movie movie;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "screen_id")
    @JsonBackReference(value = "screen-shows")
    private Screen screen;

    @NotNull(message = "Show time is required")
    @Column(name = "show_time")
    private LocalDateTime showTime;

    @NotNull(message = "Gold seat price is required")
    @Column(name = "gold_seat_price")
    private BigDecimal goldSeatPrice;

    @NotNull(message = "Silver seat price is required")
    @Column(name = "silver_seat_price")
    private BigDecimal silverSeatPrice;

    @NotNull(message = "VIP seat price is required")
    @Column(name = "vip_seat_price")
    private BigDecimal vipSeatPrice;

    @Column(name = "is_active")
    private boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "show", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Set<Booking> bookings = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Manual getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Screen getScreen() {
        return screen;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    public LocalDateTime getShowTime() {
        return showTime;
    }

    public void setShowTime(LocalDateTime showTime) {
        this.showTime = showTime;
    }

    public BigDecimal getGoldSeatPrice() {
        return goldSeatPrice;
    }

    public void setGoldSeatPrice(BigDecimal goldSeatPrice) {
        this.goldSeatPrice = goldSeatPrice;
    }

    public BigDecimal getSilverSeatPrice() {
        return silverSeatPrice;
    }

    public void setSilverSeatPrice(BigDecimal silverSeatPrice) {
        this.silverSeatPrice = silverSeatPrice;
    }

    public BigDecimal getVipSeatPrice() {
        return vipSeatPrice;
    }

    public void setVipSeatPrice(BigDecimal vipSeatPrice) {
        this.vipSeatPrice = vipSeatPrice;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(Set<Booking> bookings) {
        this.bookings = bookings;
    }

    // Constructors
    public Show() {
    }

    public Show(Movie movie, Screen screen, LocalDateTime showTime, BigDecimal goldSeatPrice,
            BigDecimal silverSeatPrice, BigDecimal vipSeatPrice) {
        this.movie = movie;
        this.screen = screen;
        this.showTime = showTime;
        this.goldSeatPrice = goldSeatPrice;
        this.silverSeatPrice = silverSeatPrice;
        this.vipSeatPrice = vipSeatPrice;
    }
}