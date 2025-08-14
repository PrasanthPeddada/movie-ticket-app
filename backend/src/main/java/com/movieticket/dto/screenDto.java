package com.movieticket.dto;

import com.movieticket.dto.screenDto;

import com.movieticket.entity.Screen;

public class screenDto {
     private Long id;
    private String name;
    private int totalRows;
    private int totalColumns;
    private String theaterName;

    // Constructors
    public screenDto(Screen screen) {
        this.id = screen.getId();
        this.name = screen.getName();
        this.totalRows = screen.getTotalRows();
        this.totalColumns = screen.getTotalColumns();
        this.theaterName = screen.getTheater().getName();
    }

    public screenDto() {
    }

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

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public int getTotalColumns() {
        return totalColumns;
    }

    public void setTotalColumns(int totalColumns) {
        this.totalColumns = totalColumns;
    }

    public String getTheaterName() {
        return theaterName;
    }

    public void setTheaterName(String theaterName) {
        this.theaterName = theaterName;
    }
}
