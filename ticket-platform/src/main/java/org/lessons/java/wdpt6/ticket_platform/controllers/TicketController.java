package org.lessons.java.wdpt6.ticket_platform.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.lessons.java.wdpt6.ticket_platform.Models.Note;
import org.lessons.java.wdpt6.ticket_platform.Models.Role;
import org.lessons.java.wdpt6.ticket_platform.Models.Ticket;
import org.lessons.java.wdpt6.ticket_platform.Models.TicketStatus;
import org.lessons.java.wdpt6.ticket_platform.Models.User;
import org.lessons.java.wdpt6.ticket_platform.repositories.CategoryRepo;
import org.lessons.java.wdpt6.ticket_platform.repositories.NoteRepo;
import org.lessons.java.wdpt6.ticket_platform.repositories.RoleRepo;
import org.lessons.java.wdpt6.ticket_platform.repositories.TicketRepo;
import org.lessons.java.wdpt6.ticket_platform.repositories.TicketStatusRepo;
import org.lessons.java.wdpt6.ticket_platform.repositories.UserRepo;
import org.lessons.java.wdpt6.ticket_platform.security.DatabaseUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @Autowired
    NoteRepo noteRepo;

    @Autowired
    CategoryRepo categoryRepo;

    @Autowired
    RoleRepo roleRepo;

    @GetMapping
    public String index(Model model, @RequestParam(required = false) String keyword,
            @AuthenticationPrincipal DatabaseUserDetails databaseUserDetails) {

        User user = userRepo.findById(databaseUserDetails.getId()).get();

        List<Role> userRoles = user.getRoles();

        List<Ticket> tickets = new ArrayList<Ticket>();

        for (Role userRole : userRoles) {
            if (userRole.getName().equals("ADMIN")) {
                if (keyword == null || keyword.isEmpty()) {
                    tickets = ticketRepo.findAll();
                } else {
                    tickets = ticketRepo.findByTitleContainingIgnoreCase(keyword);
                }
            } else {
                if (keyword == null || keyword.isEmpty()) {
                    for (Ticket ticket : ticketRepo.findAll()) {
                        if (ticket.getUser().getId().equals(user.getId())) {
                            tickets.add(ticket);
                        }
                    }
                } else {
                    for (Ticket ticket : ticketRepo.findByTitleContainingIgnoreCase(keyword)) {
                        if (ticket.getUser().getId().equals(user.getId())) {
                            tickets.add(ticket);
                        }
                    }
                }
            }
        }

        model.addAttribute("tickets", tickets);
        model.addAttribute("keyword", keyword);

        return "tickets/index";
    }

    @GetMapping("/{id}")
    public String view(Model model, @PathVariable Integer id, @AuthenticationPrincipal DatabaseUserDetails databaseUserDetails) {

        Optional<Ticket> ticketOptional = ticketRepo.findById(id);

        User user = userRepo.findById(databaseUserDetails.getId()).get();
        List<Role> userRoles = user.getRoles();

        if (!ticketOptional.get().getUser().getId().equals(databaseUserDetails.getId()) && !userRoles.contains(roleRepo.findByName("ADMIN"))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You're not allowed to perform this action");
        }

        if (ticketOptional.isPresent()) {
            model.addAttribute("ticket", ticketOptional.get());
            return "tickets/show";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There's no ticket with id: " + id);
        }
    }

    @GetMapping("/create")
    public String create(Model model) {

        List<User> users = new ArrayList<User>();

        for (User user : userRepo.findByRolesName("OPERATOR")) {
            if (user.getUserStatus().getName().equals("AVAILABLE")) {
                users.add(user);
            }
        }

        model.addAttribute("users", users);
        model.addAttribute("ticket", new Ticket());
        model.addAttribute("categories", categoryRepo.findAll());
        return "tickets/create";
    }

    @PostMapping("/create")
    public String store(Model model, @Valid @ModelAttribute("ticket") Ticket formTicket, BindingResult bindingResult) {

        List<User> users = new ArrayList<User>();

        for (User user : userRepo.findByRolesName("OPERATOR")) {
            if (user.getUserStatus().getName().equals("AVAILABLE")) {
                users.add(user);
            }
        }
        model.addAttribute("users", users);

        TicketStatus defaulTicketStatus = ticketStatusRepo.findByName("TO DO");

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryRepo.findAll());
            return "tickets/create";
        } else {
            formTicket.setTicketStatus(defaulTicketStatus);
            ticketRepo.save(formTicket);
            return "redirect:/tickets";
        }
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable Integer id, @AuthenticationPrincipal DatabaseUserDetails databaseUserDetails) {

        List<User> users = new ArrayList<User>();

        for (User user : userRepo.findByRolesName("OPERATOR")) {
            if (user.getUserStatus().getName().equals("AVAILABLE")) {
                users.add(user);
            }
        }
        model.addAttribute("users", users);

        Optional<Ticket> ticketOptional = ticketRepo.findById(id);

        User user = userRepo.findById(databaseUserDetails.getId()).get();
        List<Role> userRoles = user.getRoles();

        if (!ticketOptional.get().getUser().getId().equals(databaseUserDetails.getId()) && !userRoles.contains(roleRepo.findByName("ADMIN"))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You're not allowed to perform this action");
        }

        User assignedOperator = ticketOptional.get().getUser();

        List<TicketStatus> ticketStatuses = new ArrayList<TicketStatus>();

        if (assignedOperator.getUserStatus().getName().equals("AVAILABLE")) {
            for (TicketStatus ticketStatus : ticketStatusRepo.findAll()) {
                ticketStatuses.add(ticketStatus);
            }
        } else {
            ticketStatuses.add(ticketStatusRepo.findByName("COMPLETED"));
        }
        model.addAttribute("ticketStatuses", ticketStatuses);

        if (ticketOptional.isPresent()) {
            model.addAttribute("ticket", ticketOptional.get());
            model.addAttribute("categories", categoryRepo.findAll());
            return "tickets/edit";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There's no ticket with id: " + id);
        }
    }

    @PostMapping("/{id}/edit")
    public String update(Model model, @Valid @ModelAttribute("ticket") Ticket formTicket, BindingResult bindingResult) {

        List<User> users = new ArrayList<User>();

        for (User user : userRepo.findByRolesName("OPERATOR")) {
            if (user.getUserStatus().getName().equals("AVAILABLE")) {
                users.add(user);
            }
        }
        model.addAttribute("users", users);

        Ticket ticket = ticketRepo.findById(formTicket.getId()).get();

        User assignedOperator = ticket.getUser();

        List<TicketStatus> ticketStatuses = new ArrayList<TicketStatus>();

        if (assignedOperator.getUserStatus().getName().equals("AVAILABLE")) {
            for (TicketStatus ticketStatus : ticketStatusRepo.findAll()) {
                ticketStatuses.add(ticketStatus);
            }
        } else {
            ticketStatuses.add(ticketStatusRepo.findByName("COMPLETED"));
        }

        model.addAttribute("ticketStatuses", ticketStatuses);
        model.addAttribute("categories", categoryRepo.findAll());

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

        for (Note noteToDelete : ticketToDelete.getNotes()) {
            noteRepo.delete(noteToDelete);
        }

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