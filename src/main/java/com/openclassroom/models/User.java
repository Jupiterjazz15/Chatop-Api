package com.openclassroom.models;

import com.fasterxml.jackson.annotation.JsonIgnore; // annotation Jackson pr exclure certains champs lors de la sérialisation JSON (transfo en JSON)
import jakarta.persistence.*; // Fournit les annotations JPA pour gérer les interactions avec la DB @Entity, @Table, @Id
import jakarta.validation.constraints.Email; // Annotation @Email : format valide pour les adresses email
import jakarta.validation.constraints.NotBlank; // Annotation @NotBlank : champ obligatoire
import jakarta.validation.constraints.Size; // annotation @Size : taille minimale/maximale
import org.hibernate.annotations.CreationTimestamp; // annotation pr remplir automatiquement avec la date/heure de création de l'entité.
import org.hibernate.annotations.UpdateTimestamp;  // annotation pr mettre à jour automatiquement avec la date/heure de la dernière modification de l'entité.

import java.time.LocalDate;

@Entity // cette classe est une entitée JPA = elle sera mappée à une table
@Table(name = "users")
public class User {

    // ATTRIBUTS PRIVES DONC UNIQUEMENT DIRECTEMENT ACCESSIBLE DANS CETTE CLASSE
    @Id // cette annotation permet à l'attibut id d'être une clé primaire
    @GeneratedValue(strategy = GenerationType.IDENTITY) // valeur de id sera générée automatiquement via la stratégie d'incrémentation propre au SGBD
    private Long id;

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(max = 120)
    @JsonIgnore
    private String password;

    @CreationTimestamp
    @Column(updatable = false) // La valeur ne change pas après la création.
    private LocalDate createdAt;

    @UpdateTimestamp
    private LocalDate updatedAt;

    // CONSTRUCTEUR SANS ARGUMENT : utile pr JPA et les frameworks (Hibernate) pr de créer une instance de la classe
    public User() {}

    // CONSTRUCTEUR CLASSIQUE
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // GETTERS & SETTERS : permettent de lire ou modifier les attributs privés de la classe"
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }


}
