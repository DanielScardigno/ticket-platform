package org.lessons.java.wdpt6.ticket_platform.repositories;

import java.util.List;

import org.lessons.java.wdpt6.ticket_platform.Models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category, Integer>{

    public List<Category> findByNameContainingIgnoreCase(String name);

    public Category findByName(String name);
}