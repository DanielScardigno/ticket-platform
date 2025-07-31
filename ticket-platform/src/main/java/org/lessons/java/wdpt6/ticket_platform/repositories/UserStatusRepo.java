package org.lessons.java.wdpt6.ticket_platform.repositories;

import org.lessons.java.wdpt6.ticket_platform.Models.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStatusRepo extends JpaRepository<UserStatus, Integer>{
    
    public UserStatus findByName(String name);
}
