package com.openclassroom.dto;

import org.springframework.cglib.core.Local;

import java.time.LocalDate;

// DTO POUR TRANSPORTER LES DONNÉES USER QUE L'API RENVOIE EN REPONSE (COMME LES INFOS D'UN USER)
public class UserResponse {

    // ATTRIBUTS
    Long id;
    String name;
    String email;
    LocalDate createdAt;
    LocalDate updatedAt;

    // CONSTRUCTEUR PAR DÉFAUT : Nécessaire pr permettre à des frameworks (comme Jackson) de créer une instance sans fournir immédiatement de valeurs aux attributs.
    public UserResponse() {}

    // CONSTRUCTEUR AVEC INITIALISATION DES PARAMÈTRES :
    public UserResponse(Long id, String name, String email,LocalDate createdAt, LocalDate updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // GETTERS & SETTERS :
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

}
