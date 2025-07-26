package org.lessons.java.wdpt6.ticket_platform.repositories;

import org.lessons.java.wdpt6.ticket_platform.Models.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepo extends JpaRepository<Note, Integer> {
    
}
