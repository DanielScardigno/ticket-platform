package org.lessons.java.wdpt6.ticket_platform.controllers;

import java.util.Optional;

import org.lessons.java.wdpt6.ticket_platform.Models.Note;
import org.lessons.java.wdpt6.ticket_platform.repositories.NoteRepo;
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

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable Integer id) {

        Optional<Note> noteOptional = noteRepo.findById(id);

        if (noteOptional.isEmpty()) 
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There's no note with id: " + id);

        model.addAttribute("note", noteOptional.get());

        return "notes/edit";
    }

    @PostMapping("/{id}/edit")
    public String update(Model model, @Valid @ModelAttribute(name = "note") Note formNote, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "notes/edit";
        } else {
            noteRepo.save(formNote);
            return "redirect:/tickets";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(Model model, @PathVariable Integer id) {

        Note noteToDelete = noteRepo.findById(id).get();
        noteRepo.delete(noteToDelete);

        return "redirect:/tickets";
    }
}