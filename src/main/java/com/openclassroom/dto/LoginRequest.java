package com.openclassroom.dto;

import jakarta.validation.constraints.NotBlank;// Annotation pour spécifier que le champs ne doivent pas être vides

// DTO POUR TRANSPORTER LES DONNÉES ENVOYÉES PAR LE USER LORS D'UNE REQUETE DE CONNEXION
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
