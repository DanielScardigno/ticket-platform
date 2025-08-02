package org.lessons.java.wdpt6.ticket_platform.controllers;

import java.util.List;
import java.util.Optional;

import org.lessons.java.wdpt6.ticket_platform.Models.Category;
import org.lessons.java.wdpt6.ticket_platform.repositories.CategoryRepo;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    
    @Autowired
    CategoryRepo categoryRepo;

    @Autowired
    TicketRepo ticketRepo;

    @GetMapping
    public String index(Model model, @RequestParam(required = false) String keyword) {

        List<Category> categories;

        if (keyword == null || keyword.isEmpty()) {
            categories = categoryRepo.findAll();
        } else {
            categories = categoryRepo.findByNameContainingIgnoreCase(keyword);
        }

        model.addAttribute("categories", categories);
        model.addAttribute("keyword", keyword);

        return "categories/index";
    }

    @GetMapping("/{id}")
    public String view(Model model, @PathVariable Integer id) {

        Optional<Category> categoryOptional = categoryRepo.findById(id);

        if (categoryOptional.isPresent()) {
            model.addAttribute("category", categoryOptional.get());
            return "categories/show";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There's no category with id: " + id);
        }
    }

    @GetMapping("/create")
    public String create(Model model) {

        model.addAttribute("category", new Category());

        return "categories/create";
    }

    @PostMapping("/create")
    public String store(Model model, @Valid @ModelAttribute(name = "category") Category formCategory, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "categories/create";
        } else {
            categoryRepo.save(formCategory);
            return "redirect:/categories";
        }
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable Integer id) {

        Optional<Category> categoryOptional = categoryRepo.findById(id);

        if (categoryOptional.isPresent()) {
            model.addAttribute("category", categoryOptional.get());
            return "categories/edit";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There's no category with id: " + id);
        }
    }

    @PostMapping("/{id}/edit")
    public String update(Model model, @Valid @ModelAttribute(name = "category") Category formCategory, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "categories/edit";
        } else {
            categoryRepo.save(formCategory);
            return "redirect:/categories";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(Model model, @PathVariable Integer id) {

        Category categoryToDelete = categoryRepo.findById(id).get();
        categoryRepo.delete(categoryToDelete);

        return "redirect:/categories";
    }
}
