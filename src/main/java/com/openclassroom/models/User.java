package com.openclassroom.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
// Fournit @JsonIgnor => l'attribut password sera ignoré lors de la sérialisation/désérialisation JSON (dans les réponses API)
import jakarta.persistence.*;
// Fournit les annotations JPA pour gérer les interactions avec la DB @Entity, @Table, @Id

import jakarta.validation.constraints.Email;
// Fournit l'annotation @Email (format valide pour les adresses email)
import jakarta.validation.constraints.NotBlank;
// Fournit l'annotation @NotBlank (champ obligatoire)
import jakarta.validation.constraints.Size;
// Fournit l'annotation @Size (taille minimale/maximale)

import lombok.Data;

@Data // Lombok de générer automatiquement les mthd standard (getters, setters, etc.)
// ATTENTION COCO  : @Data est redondant ici si les getters et setters sont écrits manuellement.
@Entity // cette classe est une entitée JPA = elle sera mappée à une table
@Table(name = "users")
public class User {
    // ATTRIBUTS en privée cad uniquement directement accessible dans cette classe

    @Id // le champ id est la clé primaire de la table grâce a cette annotation
    @GeneratedValue(strategy = GenerationType.IDENTITY) // la valeur de id sera générée automatiquement via la stratégie d'incrémentation propre au SGBD
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

    // CONSTRUCEUR
    // Constructeur sans argument : utile pr JPA et les frameworks (Hibernate) pr de créer une instance de la classe.
    public User() {}

    // Constructeur avec paramètre pour initialiser un objet User avec des valeurs pour username, email, et password.
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

}
