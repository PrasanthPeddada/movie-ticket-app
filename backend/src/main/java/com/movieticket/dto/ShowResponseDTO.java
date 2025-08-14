package com.movieticket.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.movieticket.entity.Screen;
import com.movieticket.entity.Show;
import com.movieticket.entity.Theater;
import lombok.Data;

public class ShowResponseDTO {
    private Long id;
    private LocalDateTime showTime;
    private BigDecimal goldSeatPrice;
    private BigDecimal silverSeatPrice;
    private BigDecimal vipSeatPrice;
    private boolean active;

    private ScreenDTO screen;

    // Constructors, Getters, and Setters

    @Data
    public static class ScreenDTO {
        private Long id;
        private String name;
        private TheaterDTO theater;

        public ScreenDTO(Screen screen) {
            this.id = screen.getId();
            this.name = screen.getName();
            this.theater = new TheaterDTO(screen.getTheater());
        }
    }

    @Data
    public static class TheaterDTO {
        private Long id;
        private String name;

        public TheaterDTO(Theater theater) {
            this.id = theater.getId();
            this.name = theater.getName();
        }
    }

    public ShowResponseDTO(Show show) {
        this.id = show.getId();
        this.showTime = show.getShowTime();
        this.goldSeatPrice = show.getGoldSeatPrice();
        this.silverSeatPrice = show.getSilverSeatPrice();
        this.vipSeatPrice = show.getVipSeatPrice();
        this.active = show.isActive();
        this.screen = new ScreenDTO(show.getScreen());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public ScreenDTO getScreen() {
        return screen;
    }

    public void setScreen(ScreenDTO screen) {
        this.screen = screen;
    }
}
