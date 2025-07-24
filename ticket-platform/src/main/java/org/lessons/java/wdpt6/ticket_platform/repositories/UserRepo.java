package org.lessons.java.wdpt6.ticket_platform.repositories;

import java.util.List;

import org.lessons.java.wdpt6.ticket_platform.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Integer>{
    
    public List<User> findByUserNameContainingIgnoreCase(String userName);

    public List<User> findByRolesName(String name);
}