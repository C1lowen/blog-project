package com.users.users.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity(name = "views")
@Data
@NoArgsConstructor
public class Views {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;
    @Column(name = "post_id")
    private int postId;
    @Column(name = "count_view")
    private int view;

    public Views(int postId, int view) {
        this.postId = postId;
        this.view = view;
    }
}
