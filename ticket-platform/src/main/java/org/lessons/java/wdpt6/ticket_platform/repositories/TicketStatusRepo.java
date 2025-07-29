package org.lessons.java.wdpt6.ticket_platform.repositories;


import org.lessons.java.wdpt6.ticket_platform.Models.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketStatusRepo extends JpaRepository<TicketStatus, Integer>{

    public TicketStatus findByName(String name);
}