package com.movieticket.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "booked_seats", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"show_id", "row_number", "column_number"})
})
public class BookedSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    @com.fasterxml.jackson.annotation.JsonBackReference
    private Booking booking;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "show_id")
    @JsonIgnore
    private Show show;

    @NotNull(message = "Row number is required")
    @Positive(message = "Row number must be positive")
    @Column(name = "`row_number`")
    private Integer rowNumber;

    @NotNull(message = "Column number is required")
    @Positive(message = "Column number must be positive")
    @Column(name = "column_number")
    private Integer columnNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "seat_category")
    private SeatCategory seatCategory;

    @NotNull(message = "Price is required")
    @Column(name = "price")
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SeatStatus status = SeatStatus.BOOKED;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum SeatCategory {
        GOLD, SILVER, VIP
    }

    public enum SeatStatus {
        AVAILABLE, BOOKED, HOLD, MAINTENANCE
    }

    // Manual getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    public Integer getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }

    public Integer getColumnNumber() {
        return columnNumber;
    }

    public void setColumnNumber(Integer columnNumber) {
        this.columnNumber = columnNumber;
    }

    public SeatCategory getSeatCategory() {
        return seatCategory;
    }

    public void setSeatCategory(SeatCategory seatCategory) {
        this.seatCategory = seatCategory;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public SeatStatus getStatus() {
        return status;
    }

    public void setStatus(SeatStatus status) {
        this.status = status;
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

    // Constructors
    public BookedSeat() {
    }

    public BookedSeat(Booking booking, Show show, Integer rowNumber, Integer columnNumber, SeatCategory seatCategory,
            BigDecimal price) {
        this.booking = booking;
        this.show = show;
        this.rowNumber = rowNumber;
        this.columnNumber = columnNumber;
        this.seatCategory = seatCategory;
        this.price = price;
    }
}