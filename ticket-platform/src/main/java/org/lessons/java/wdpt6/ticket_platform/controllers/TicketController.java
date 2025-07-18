package org.lessons.java.wdpt6.ticket_platform.controllers;

import java.util.Optional;

import org.lessons.java.wdpt6.ticket_platform.Models.Ticket;
import org.lessons.java.wdpt6.ticket_platform.repositories.TicketRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;

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

    
    @GetMapping("/{id}")
    public String view(Model model, @PathVariable Integer id) {
        
        Optional<Ticket> tickeOptional = ticketRepo.findById(id);
        
        if (tickeOptional.isPresent()) {
            model.addAttribute("ticket", tickeOptional.get());   
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There's no ticket with id: " + id);
        }
        
        return "tickets/show";
    }

    @GetMapping("/create")
    public String create(Model model) {
    
        model.addAttribute("ticket", new Ticket());
    
        return "tickets/create";
    }
    
    @PostMapping("/create")
    public String store(Model model, @Valid @ModelAttribute("ticket") Ticket formTicket, BindingResult bindingResult) {
    
        if (bindingResult.hasErrors()) {
            return "tickets/create";
        } else {
            ticketRepo.save(formTicket);
            return "redirect:/tickets";
        }
    }
}
