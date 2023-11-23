package com.users.users.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;
@Entity(name = "comments")
@Data
@NoArgsConstructor
public class Comments {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;
    @Size(min = 3, max = 15)
    private String name;
    private String message;
    private Integer post;
    private LocalDate date;

    public Comments( String name, String message, Integer post, LocalDate date) {
        this.name = name;
        this.message = message;
        this.post = post;
        this.date = date;
    }
}
