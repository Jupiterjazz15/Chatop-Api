package com.openclassroom.dto;

// DTO POUR TRANSPORTER LES DONNÉES USER QUE L'API RENVOIE EN REPONSE (COMME LES INFOS D'UN USER)
public class UserResponse {

    // ATTRIBUTS
    Long id;
    String name;
    String email;

    // CONSTRUCTEUR PAR DÉFAUT : Nécessaire pr permettre à des frameworks (comme Jackson) de créer une instance sans fournir immédiatement de valeurs aux attributs.
    public UserResponse() {}

    // CONSTRUCTEUR AVEC INITIALISATION DES PARAMÈTRES :
    public UserResponse(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
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
}
