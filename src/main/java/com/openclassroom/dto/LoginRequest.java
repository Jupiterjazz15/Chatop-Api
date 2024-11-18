package com.openclassroom.dto;

import jakarta.validation.constraints.NotBlank;
// @NotBlank => annotation pour spécifier que le champs ne doivent pas être vides

// DTO (Data Transfer Object) utilisé pour transporter les données envoyées par l'utilisateur lors d'une requête de connexion (login)
public class LoginRequest {

    // déclaration des attributs
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    // GETTERS & SETTERS
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
