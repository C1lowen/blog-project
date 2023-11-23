package com.users.users.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import javax.validation.constraints.Size;


import static jakarta.persistence.GenerationType.IDENTITY;

@Entity(name = "users")
@Data
@NoArgsConstructor
public class CustomUser {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;
    @Size(min=3, max=15)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Role role;
    @Size(min=8, max=50)
    private String password;

    private String email;

    private String instagram;
    private String facebook;
    private String telegram;
    public CustomUser(String name, Role role, String password, String email) {
        this.name = name;
        this.role = role;
        this.password = password;
        this.email = email;
    }

    public String toString() {
        return "";
    }
}
