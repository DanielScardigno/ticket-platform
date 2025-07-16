package org.lessons.java.wdpt6.ticket_platform.controllers;

import org.lessons.java.wdpt6.ticket_platform.repositories.TicketRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tickets")
public class TicketController {
    
    @Autowired
    TicketRepo ticketRepo;

    @GetMapping
    public String index(Model model) {
        
        model.addAttribute("tickets", ticketRepo.findAll());

        return "tickets/index";
    }
}
