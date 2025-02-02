package org.example.stavkova_kancelaria_web.sportevent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class SportEventController {
    private final SportEventDAO sportEventDAO;

    @Autowired
    public SportEventController(SportEventDAO sportEventDAO) {
        this.sportEventDAO = sportEventDAO;
    }

    @GetMapping("/sportevents")
    public List<SportEvent> getAllSportEvents() {
        return sportEventDAO.getAllSportEvents();
    }

    @GetMapping("/sportevents/{id}")
    public SportEvent findById(@PathVariable int id) {
        return sportEventDAO.findById(id);
    }

    @PutMapping("/sportevents/hide/{id}")
    public void hideEvent(@PathVariable int id) {
            sportEventDAO.hideEvent(id);
    }
    @PutMapping("/sportevents/update/{id}")
    public void updateEventStatus(@PathVariable int id) {
            sportEventDAO.updateEventStatus(id);
    }

    @PostMapping(value = "/sportevents")
    public ResponseEntity<?> createEvent(@RequestBody SportEvent sportEvent) {
        int eventId = sportEventDAO.createEvent(sportEvent);
        return ResponseEntity.status(HttpStatus.CREATED).body(eventId);
    }
}
