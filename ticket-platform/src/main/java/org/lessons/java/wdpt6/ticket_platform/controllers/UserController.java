package org.lessons.java.wdpt6.ticket_platform.controllers;

import java.util.List;
import java.util.Optional;

import org.lessons.java.wdpt6.ticket_platform.Models.User;
import org.lessons.java.wdpt6.ticket_platform.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;


@Controller
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    UserRepo userRepo;

    @GetMapping
    public String index(Model model, @RequestParam(required = false) String keyword) {

        List<User> users;

        if (keyword == null || keyword.isEmpty()) {
            users = userRepo.findAll();
        } else {
            users = userRepo.findByUserNameContainingIgnoreCase(keyword);
        }

        model.addAttribute("users", users);
        model.addAttribute("keyword", keyword);

        return "users/index";
    }

    @GetMapping("/{id}")
    public String view(Model model, @PathVariable Integer id) {

        Optional<User> userOptional = userRepo.findById(id);

        if (userOptional.isPresent()) {
            model.addAttribute("user", userOptional.get());
            return "users/show";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
