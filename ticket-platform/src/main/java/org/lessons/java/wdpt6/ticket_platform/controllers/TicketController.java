package org.lessons.java.wdpt6.ticket_platform.controllers;

import java.util.List;
import java.util.Optional;

import org.lessons.java.wdpt6.ticket_platform.Models.Note;
import org.lessons.java.wdpt6.ticket_platform.Models.Ticket;
import org.lessons.java.wdpt6.ticket_platform.Models.TicketStatus;
import org.lessons.java.wdpt6.ticket_platform.repositories.TicketRepo;
import org.lessons.java.wdpt6.ticket_platform.repositories.TicketStatusRepo;
import org.lessons.java.wdpt6.ticket_platform.repositories.UserRepo;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;


import jakarta.validation.Valid;

@Controller
@RequestMapping("/tickets")
public class TicketController {
    
    @Autowired
    TicketRepo ticketRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    TicketStatusRepo ticketStatusRepo;

    @GetMapping
    public String index(Model model, @RequestParam(required = false) String keyword) {

        List<Ticket> tickets;

        if (keyword == null || keyword.isEmpty()) {
            tickets = ticketRepo.findAll();
        } else {
            tickets = ticketRepo.findByTitleContainingIgnoreCase(keyword);
        }
        
        model.addAttribute("tickets", tickets);
        model.addAttribute("keyword", keyword);

        return "tickets/index";
    }

    
    @GetMapping("/{id}")
    public String view(Model model, @PathVariable Integer id) {
        
        Optional<Ticket> ticketOptional = ticketRepo.findById(id);
        
        if (ticketOptional.isPresent()) {
            model.addAttribute("ticket", ticketOptional.get());   
            return "tickets/show";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There's no ticket with id: " + id);
        }
    }

    @GetMapping("/create")
    public String create(Model model) {
    
        model.addAttribute("users", userRepo.findByRolesName("OPERATOR"));

        model.addAttribute("ticket", new Ticket());

        return "tickets/create";
    }
    
    @PostMapping("/create")
    public String store(Model model, @Valid @ModelAttribute("ticket") Ticket formTicket, BindingResult bindingResult) {

        model.addAttribute("users", userRepo.findByRolesName("OPERATOR"));

        TicketStatus defaulTicketStatus = ticketStatusRepo.findByName("TO DO");
    
        if (bindingResult.hasErrors()) {
            return "tickets/create";
        } else {
            formTicket.setTicketStatus(defaulTicketStatus);
            ticketRepo.save(formTicket);
            return "redirect:/tickets";
        }
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable Integer id) {

        model.addAttribute("users", userRepo.findByRolesName("OPERATOR"));
        model.addAttribute("ticketStatuses", ticketStatusRepo.findAll());

        Optional<Ticket> ticketOptional = ticketRepo.findById(id);
        
        if (ticketOptional.isPresent()) {
            model.addAttribute("ticket", ticketOptional.get());   
            return "tickets/edit";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There's no ticket with id: " + id);
        }
    }

    @PostMapping("/{id}/edit")
    public String update(Model model, @Valid @ModelAttribute("ticket") Ticket formTicket, BindingResult bindingResult) {

        model.addAttribute("users", userRepo.findByRolesName("OPERATOR"));
        model.addAttribute("ticketStatuses", ticketStatusRepo.findAll());
        
        if (bindingResult.hasErrors()) {
            return "tickets/edit";
        } else {
            ticketRepo.save(formTicket);
            return "redirect:/tickets";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(Model model, @PathVariable Integer id) {

        Ticket ticketToDelete = ticketRepo.findById(id).get();

        ticketRepo.delete(ticketToDelete);
        return "redirect:/tickets";
    }

    @GetMapping("/{id}/create")
    public String addNote(Model model, @PathVariable Integer id) {
        
        Optional<Ticket> ticketOptional = ticketRepo.findById(id);

        if (ticketOptional.isEmpty()) 
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There's no ticket with id: " + id);

        model.addAttribute("ticket", ticketOptional.get());

        Note newNote = new Note();
        newNote.setTicket(ticketOptional.get());

        model.addAttribute("note", newNote);

        return "notes/create";
    }
}