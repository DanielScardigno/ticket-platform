package org.lessons.java.wdpt6.ticket_platform.Models;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

    @ManyToOne
    @JoinColumn(name = "ticket_status_id", nullable = false)
    @JsonBackReference
    private TicketStatus ticketStatus;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @NotNull(message = "You must select a category")
    @JsonBackReference
    private Category category;

    @OneToMany(mappedBy = "ticket")
    private List<Note> notes;

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

    public TicketStatus getTicketStatus() {
        return this.ticketStatus;
    }

    public void setTicketStatus(TicketStatus ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Note> getNotes() {
        return this.notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }
}