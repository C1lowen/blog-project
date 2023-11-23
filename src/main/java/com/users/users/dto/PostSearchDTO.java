package com.users.users.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class PostSearchDTO {
    private int id;

    private String name;

    private LocalDate date;
    private String title;
    private String text;
    private String folder;
    private Integer index;
    private Integer countComments;
    private Integer views;
    public PostSearchDTO(int id, String customUser, LocalDate date, String title, String text, String folder, Integer countComments) {
        this.id = id;
        this.name = customUser;
        this.date = date;
        this.title = title;
        this.text = text;
        this.folder = folder;
        this.index = 0;
        this.countComments = countComments;
    }


}
