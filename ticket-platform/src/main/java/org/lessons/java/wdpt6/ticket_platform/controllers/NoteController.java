package org.lessons.java.wdpt6.ticket_platform.controllers;

import org.lessons.java.wdpt6.ticket_platform.Models.Note;
import org.lessons.java.wdpt6.ticket_platform.repositories.NoteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/notes")
public class NoteController {

    @Autowired
    NoteRepo noteRepo;
    
    @PostMapping("/create")
    public String store(Model model, @Valid @ModelAttribute(name = "note") Note formNote, BindingResult bindingResult) {

        model.addAttribute("note", formNote);

        if (bindingResult.hasErrors()) {
            return "notes/create";
        } else {
            noteRepo.save(formNote);
            return "redirect:/tickets";
        }
    }
}
