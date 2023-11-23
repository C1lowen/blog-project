package com.users.users.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;

@Data
public class PostDTO {
    private int id;

    private String name;

    private LocalDate date;
    private String title;
    private String text;
    private String folder;
    private Integer countComments;
    private Integer views;
    public PostDTO(int id, String customUser, LocalDate date, String title, String text, String folder, Integer countComments) {
        this.id = id;
        this.name = customUser;
        this.date = date;
        this.title = title;
        this.text = text;
        this.folder = folder;
        this.countComments = countComments;
    }
}

