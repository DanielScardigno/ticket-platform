package org.lessons.java.wdpt6.ticket_platform.repositories;

import java.util.List;
import java.util.Optional;

import org.lessons.java.wdpt6.ticket_platform.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Integer>{

    Optional<User> findByUserName(String userName);
    
    public List<User> findByUserNameContainingIgnoreCase(String userName);

    public List<User> findByRolesName(String name);
}