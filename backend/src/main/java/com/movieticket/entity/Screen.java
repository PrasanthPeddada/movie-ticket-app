package com.movieticket.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonBackReference;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "screens")
public class Screen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Screen name is required")
    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "theater_id")
    @JsonBackReference(value = "theater-screens")
    private Theater theater;

    @NotNull(message = "Number of rows is required")
    @Positive(message = "Number of rows must be positive")
    @Column(name = "total_rows")
    private Integer totalRows;

    @NotNull(message = "Number of columns is required")
    @Positive(message = "Number of columns must be positive")
    @Column(name = "total_columns")
    private Integer totalColumns;

    @Column(name = "is_active")
    private boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "screen", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "screen-shows")
    private Set<Show> shows = new HashSet<>();

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Theater getTheater() {
        return theater;
    }

    public void setTheater(Theater theater) {
        this.theater = theater;
    }

    public Integer getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(Integer totalRows) {
        this.totalRows = totalRows;
    }

    public Integer getTotalColumns() {
        return totalColumns;
    }

    public void setTotalColumns(Integer totalColumns) {
        this.totalColumns = totalColumns;
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

    public Set<Show> getShows() {
        return shows;
    }

    public void setShows(Set<Show> shows) {
        this.shows = shows;
    }

    // Constructors
    public Screen() {
    }

    public Screen(String name, Theater theater, Integer totalRows, Integer totalColumns) {
        this.name = name;
        this.theater = theater;
        this.totalRows = totalRows;
        this.totalColumns = totalColumns;
    }
}