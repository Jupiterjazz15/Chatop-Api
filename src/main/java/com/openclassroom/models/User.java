package com.openclassroom.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.Entity;

@Entity
@Table(name = "USERS") // Assure-toi que le nom correspond à ta base de données
public class User {

    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Getter
    @Setter
    @Column(nullable = false, unique = true)
    private String email;

    @Setter
    @Getter
    @Column(nullable = false)
    private String password;

    // Constructeurs, getters et setters
    public User() {
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return name;
    }

    public void setUsername(String name) {
        this.name = name;
    }

}
