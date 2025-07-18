package org.lessons.java.wdpt6.ticket_platform.repositories;

import org.lessons.java.wdpt6.ticket_platform.Models.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepo extends JpaRepository<Ticket, Integer>{
    
}
