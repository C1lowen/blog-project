package com.users.users.dto;



import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserDTO {

    private int id;
    @Length(min=3, max=15)
    @NotBlank
    private String name;

    private String role;
    @Size(min=8)
    private String password;
    @Size(max=100)
    private String email;

    private String instagram;
    private String facebook;
    private String telegram;

    public UserDTO(int id, String name, String role, String password, String email, String instagram, String facebook, String telegram) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.password = password;
        this.email = email;
        this.instagram = instagram;
        this.facebook = facebook;
        this.telegram = telegram;
    }

    public UserDTO() {
    }
}
