package org.lessons.java.wdpt6.ticket_platform.controllers.api;

import java.util.ArrayList;
import java.util.List;

import org.lessons.java.wdpt6.ticket_platform.Models.Ticket;
import org.lessons.java.wdpt6.ticket_platform.repositories.TicketRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tickets")
public class TicketRestController {

    @Autowired
    TicketRepo ticketRepo;

    @GetMapping
    public ResponseEntity<List<Ticket>> index(@RequestParam(required = false) String categoryFilter, @RequestParam(required = false) String statusFilter) {

        List<Ticket> tickets = new ArrayList<Ticket>();

        if (categoryFilter != null && !categoryFilter.isEmpty() && !categoryFilter.isBlank() && statusFilter != null && !statusFilter.isEmpty() && !statusFilter.isBlank()) {
            for (Ticket filteredTicket : ticketRepo.findByCategoryName(categoryFilter)) {
                if (filteredTicket.getTicketStatus().getName().equals(statusFilter)) {
                    tickets.add(filteredTicket);
                    break;
                }
            }
        } else if (categoryFilter != null && !categoryFilter.isEmpty() && !categoryFilter.isBlank()) {
                tickets = ticketRepo.findByCategoryName(categoryFilter);
        } else if (statusFilter != null && !statusFilter.isEmpty() && !statusFilter.isBlank()) {
                tickets = ticketRepo.findByTicketStatusName(statusFilter);
        } else {
            tickets = ticketRepo.findAll();
        }
    
        if (tickets.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Ticket>>(tickets, HttpStatus.OK);
    }
}