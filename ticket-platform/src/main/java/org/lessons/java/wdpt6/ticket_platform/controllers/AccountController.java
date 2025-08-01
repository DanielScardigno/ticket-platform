package org.lessons.java.wdpt6.ticket_platform.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.lessons.java.wdpt6.ticket_platform.Models.Ticket;
import org.lessons.java.wdpt6.ticket_platform.Models.User;
import org.lessons.java.wdpt6.ticket_platform.Models.UserStatus;
import org.lessons.java.wdpt6.ticket_platform.repositories.RoleRepo;
import org.lessons.java.wdpt6.ticket_platform.repositories.UserRepo;
import org.lessons.java.wdpt6.ticket_platform.repositories.UserStatusRepo;
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
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    UserStatusRepo userStatusRepo;

    @Autowired
    RoleRepo roleRepo;
    
    @GetMapping
    public String view(Model model, @AuthenticationPrincipal DatabaseUserDetails databaseUserDetails) {

        User user = userRepo.findById(databaseUserDetails.getId()).get();

        model.addAttribute("user", user);
        return "account/show";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable Integer id, @AuthenticationPrincipal DatabaseUserDetails databaseUserDetails) {

        Optional<User> userOptional = userRepo.findById(id);
       
        if (userOptional.isEmpty()) 
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There's no user with id: " + id);

        User user = userOptional.get();

        if (!id.equals(databaseUserDetails.getId()))
            throw new SecurityException("You're not authorized to perform this action");

        List<Ticket> userTickets = user.getTickets();

        Boolean isTicketUncompleted = false;

        for (Ticket userTicket : userTickets) {
            if (userTicket.getTicketStatus().getName().equals("TO DO") || userTicket.getTicketStatus().getName().equals("IN PROGRESS")) {
                isTicketUncompleted = true;
                break;
            }
        }

        List<UserStatus> userStatuses = new ArrayList<UserStatus>();

        if (isTicketUncompleted == true) {
            userStatuses.add(userStatusRepo.findByName("AVAILABLE"));
        } else {
            for (UserStatus userStatus : userStatusRepo.findAll()) {
                userStatuses.add(userStatus);
            }
        }
    
        model.addAttribute("user", user);
        model.addAttribute("userStatuses", userStatuses);
        model.addAttribute("roles", roleRepo.findAll());
        return "account/edit";
    }

    @PostMapping("/{id}/edit")
    public String update(Model model, @Valid @ModelAttribute(name = "user") User formUser, BindingResult bindingResult) {

        User user = userRepo.findById(formUser.getId()).get();

        List<Ticket> userTickets = user.getTickets();

        Boolean isTicketUncompleted = false;

        for (Ticket userTicket : userTickets) {
            if (userTicket.getTicketStatus().getName().equals("TO DO") || userTicket.getTicketStatus().getName().equals("IN PROGRESS")) {
                isTicketUncompleted = true;
                break;
            }
        }

        List<UserStatus> userStatuses = new ArrayList<UserStatus>();

        if (isTicketUncompleted == true) {
            userStatuses.add(userStatusRepo.findByName("AVAILABLE"));
        } else {
            for (UserStatus userStatus : userStatusRepo.findAll()) {
                userStatuses.add(userStatus);
            }
        }

        model.addAttribute("userStatuses", userStatuses);
        model.addAttribute("roles", roleRepo.findAll());

        if (bindingResult.hasErrors()) {
            return "account/edit";
        } else {
            userRepo.save(formUser);
            return "redirect:/tickets";
        }
    }
}