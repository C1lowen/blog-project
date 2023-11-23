package com.users.users.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity(name = "post")
@Data
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;

    private Integer name;

    private LocalDate date;
    private String title;
    private String text;
    private String folder;
    @Column(name = "comment")
    private Integer countComments;

    public Post(int id, Integer customUser, LocalDate date, String title, String text, String folder) {
        this.id = id;
        this.name = customUser;
        this.date = date;
        this.title = title;
        this.text = text;
        this.folder = folder;
    }
}
