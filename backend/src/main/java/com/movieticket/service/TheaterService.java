package com.movieticket.service;

import com.movieticket.entity.Screen;
import com.movieticket.entity.Theater;
import com.movieticket.repository.TheaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TheaterService {

    @Autowired
    private TheaterRepository theaterRepository;

    @Autowired
    private ScreenService screenService;

    public List<Theater> getAllActiveTheaters() {
        return theaterRepository.findByIsActiveTrue();
    }

    public Theater getTheaterById(Long id) {
        return theaterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Theater not found"));
    }

    public List<Theater> getTheatersByCity(String city) {
        return theaterRepository.findByCityAndIsActiveTrue(city);
    }

    public List<Theater> getTheatersByState(String state) {
        return theaterRepository.findByStateAndIsActiveTrue(state);
    }

    public List<Theater> getTheatersByCityAndState(String city, String state) {
        return theaterRepository.findByCityAndState(city, state);
    }

    public List<String> getAllCities() {
        return theaterRepository.findAllCities();
    }

    public List<String> getAllStates() {
        return theaterRepository.findAllStates();
    }

    public List<Theater> searchTheaters(String city,String state){
        List<Theater> theaters;
        if (city != null && state != null) {
            theaters = getTheatersByCityAndState(city, state);
        } else if (city != null) {
            theaters = getTheatersByCity(city);
        } else if (state != null) {
            theaters = getTheatersByState(state);
        } else {
            theaters = getAllActiveTheaters();
        }
        return theaters;
    }

    public Theater createTheater(Theater theater) {

         
    theater.setActive(true);

    // First, save the theater without screens (to generate its ID)
    Theater savedTheater = theaterRepository.save(theater);

    Set<Screen> screenArray = theater.getScreens();
    if (screenArray == null || screenArray.isEmpty()) {
        throw new RuntimeException("At least one screen is required for the theater");
    }

    for (Screen screen : screenArray) {
        screen.setTheater(savedTheater); // Set the FK
        screenService.createScreen(screen); // Save each screen
        savedTheater.addScreen(screen);    // Optional: link back to theater
    }

    return theaterRepository.save(savedTheater);
}


    public Theater updateTheater(Long id, Theater theaterDetails) {
        Theater theater = getTheaterById(id);

        theater.setName(theaterDetails.getName());
        theater.setAddress(theaterDetails.getAddress());
        theater.setCity(theaterDetails.getCity());
        theater.setState(theaterDetails.getState());
        theater.setPincode(theaterDetails.getPincode());
        theater.setPhoneNumber(theaterDetails.getPhoneNumber());
        theater.setUpdatedAt(LocalDateTime.now());

        return theaterRepository.save(theater);
    }

    public void deleteTheater(Long id) {
        Theater theater = getTheaterById(id);
        theater.setActive(false);
        theater.setUpdatedAt(LocalDateTime.now());
        theaterRepository.save(theater);
    }
}