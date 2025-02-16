package org.example.stavkova_kancelaria_web.sportevent;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SportEvent {
    private Integer eventId;
    private String eventName;
    private LocalDateTime startTime;
    private String sportType;
    private StatusForEvent status;
    @Override
    public String toString() {
        return eventName + ", " + sportType + ", "
                + startTime.toLocalDate() + ", " + startTime.toLocalTime() + ", " + status;
    }
}

