package com.users.users.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity(name = "answers")
@Data
@NoArgsConstructor
public class Answer {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;
    @Size(min = 3, max = 15)
    private String name;
    private String subject;
    private String email;
    @Size(min = 8, max = 50)
    private String message;

    public Answer(String name, String subject, String email, String message) {
        this.name = name;
        this.subject = subject;
        this.email = email;
        this.message = message;
    }
}
