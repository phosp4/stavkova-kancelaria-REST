package org.example.stavkova_kancelaria_web.ticket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TicketController {

    private final TicketDAO ticketDAO;

    @Autowired
    public TicketController(TicketDAO ticketDAO) {
        this.ticketDAO = ticketDAO;
    }

    @PostMapping("/tickets")
    public void createTicket(@RequestBody Ticket ticket) {
        ticketDAO.insertTicket(ticket);
    }

    @GetMapping("/tickets")
    public List<Ticket> findAllTickets() {
        return ticketDAO.findAllTickets();
    }

    @GetMapping("/tickets/{id}")
    public Ticket findTicketById(@PathVariable int id) {
        return ticketDAO.findTicketById(id);
    }

    @PutMapping("/tickets")
    public void updateTicket(@RequestBody Ticket ticket) {
        ticketDAO.updateTicket(ticket);
    }

    @DeleteMapping("/tickets/{id}")
    public void deleteTicket(@PathVariable int id) {
        ticketDAO.deleteTicket(id);
    }
}
