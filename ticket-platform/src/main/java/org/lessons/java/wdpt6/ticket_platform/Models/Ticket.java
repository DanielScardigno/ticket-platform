package org.lessons.java.wdpt6.ticket_platform.Models;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


@Entity
@Table(name = "tickets")
public class Ticket {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Title cannot be empty or contain only spaces")
    @Size(max = 30, message = "Title cannot be longer than 30 characters")
    private String title;

    @Lob
    @NotBlank(message = "Body cannot be empty or contain only spaces")
    private String body;

    private LocalDate creationDate = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull(message = "You must select an operator")
    @JsonBackReference
    private User user;

    // Getters and Setters

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDate getCreationDate() {
        return this.creationDate;
    }

    
    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}