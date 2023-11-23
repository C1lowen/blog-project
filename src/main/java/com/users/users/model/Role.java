package com.users.users.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.List;


import static jakarta.persistence.GenerationType.IDENTITY;

@Entity(name = "roles")
@Data
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;
    private String name;

    @OneToMany(mappedBy = "role", fetch = FetchType.EAGER)
    private List<CustomUser> users;

    public Role(int id) {
        this.id = id;
    }


}
