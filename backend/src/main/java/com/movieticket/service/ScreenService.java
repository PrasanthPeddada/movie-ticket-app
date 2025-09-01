package com.movieticket.service;

import com.movieticket.dto.screenDto;
import com.movieticket.entity.Screen;
import com.movieticket.entity.Theater;
import com.movieticket.repository.ScreenRepository;
import com.movieticket.repository.TheaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScreenService {

    @Autowired
    private ScreenRepository screenRepository;

    @Autowired
    private TheaterRepository theaterRepository;

    public List<screenDto> getAllActiveScreens() {
        return screenRepository.findAll().stream()
                .filter(Screen::isActive)
                .map(screenDto::new) // Convert Screen to screenDto
                .collect(java.util.stream.Collectors.toList());
    }

    public Screen getScreenById(Long id) {
        return screenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Screen not found"));
    }

    public List<Screen> getScreensByTheater(Long theaterId) {
        return screenRepository.findByTheaterIdAndIsActiveTrue(theaterId);
    }

    
    public Screen createScreen(Screen screen) {
        // Verify theater exists
        Theater theater = theaterRepository.findById(screen.getTheater().getId()).orElseThrow(() ->  new RuntimeException("theater not found"));
                

        screen.setTheater(theater);
        screen.setActive(true);
        return screenRepository.save(screen);
    }

    public Screen updateScreen(Long id, Screen screenDetails) {
        Screen screen = getScreenById(id);

        screen.setName(screenDetails.getName());
        screen.setTotalRows(screenDetails.getTotalRows());
        screen.setTotalColumns(screenDetails.getTotalColumns());
        screen.setUpdatedAt(LocalDateTime.now());

        return screenRepository.save(screen);
    }

    public void deleteScreen(Long id) {
        Screen screen = getScreenById(id);
        screen.setActive(false);
        screen.setUpdatedAt(LocalDateTime.now());
        screenRepository.save(screen);
    }

   
}