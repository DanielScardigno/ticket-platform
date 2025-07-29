package org.lessons.java.wdpt6.ticket_platform.controllers;

import java.util.List;
import java.util.Optional;

import org.lessons.java.wdpt6.ticket_platform.Models.User;
import org.lessons.java.wdpt6.ticket_platform.repositories.RoleRepo;
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
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    UserRepo userRepo;

    @Autowired
    RoleRepo roleRepo;

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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There's no User with id " + id);
        }
    }

    @GetMapping("/create")
    public String create(Model model) {

        model.addAttribute("user", new User());
        model.addAttribute("roles", roleRepo.findAll());

        return "users/create";
    }

    @PostMapping("/create")
    public String store(Model model, @Valid @ModelAttribute(name = "user") User formUser, BindingResult bindingResult) {

        model.addAttribute("roles", roleRepo.findAll());

        if (bindingResult.hasErrors()) {
            return "users/create";
        } else {
            userRepo.save(formUser);
            return "redirect:/users";
        }
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable Integer id) {

        model.addAttribute("roles", roleRepo.findAll());

        Optional<User> userOptional = userRepo.findById(id);

        if (userOptional.isPresent()) {
            model.addAttribute("user", userOptional.get());
            return "users/edit";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There's no User with id " + id);
        }
    }

    @PostMapping("/{id}/edit")
    public String update(Model model, @Valid @ModelAttribute(name = "user") User formUser, BindingResult bindingResult) {

        model.addAttribute("roles", roleRepo.findAll());

        if (bindingResult.hasErrors()) {
            return "users/edit";
        } else {
            userRepo.save(formUser);
            return "redirect:/users";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(Model model, @PathVariable Integer id) {

        User userToDelete = userRepo.findById(id).get();

        userRepo.delete(userToDelete);
        return "redirect:/users";
    }
}