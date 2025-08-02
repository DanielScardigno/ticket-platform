package org.lessons.java.wdpt6.ticket_platform.repositories;


import org.lessons.java.wdpt6.ticket_platform.Models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Integer>{
    public Role findByName(String name);
}