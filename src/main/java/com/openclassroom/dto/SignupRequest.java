package com.openclassroom.dto;

import jakarta.validation.constraints.*;
// Import des annotations pour la validation des champs.

// Classe DTO (Data Transfer Object) utilisé pour transporter les données d'inscription envoyées par un utilisateur.
public class SignupRequest {
    // Déclaration des attributs
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    //private Set<String> role;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    // GETTERS & SETTERS
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
