// src/main/java/org/example/stavkova_kancelaria_web/ticket/Ticket.java
package org.example.stavkova_kancelaria_web.ticket;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.stavkova_kancelaria_web.possibleoutcome.PossibleOutcome;
import org.example.stavkova_kancelaria_web.user.User;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class Ticket {
    private Integer ticketId;
    private User user;
    private PossibleOutcome possibleOutcome;
    private StatusForTicket status;
    private Double stake;
    private LocalDateTime eventStartTime;
}