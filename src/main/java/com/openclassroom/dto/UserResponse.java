package com.openclassroom.dto;

// Data Transfer Object (DTO) utilisée pr transporter les données user que l'API renvoie en réponse,
// comme les informations d'un utilisateur.

public class UserResponse {

    // Déclaration des attributs
    Long id;
    String name;
    String email;

    // CONSTRUCTEUR PAR DÉFAUT :
    // Nécessaire pr permettre à des frameworks (comme Jackson) de créer une instance sans fournir immédiatement de valeurs aux attributs.
    public UserResponse() {}

    // CONSTRUCTEUR AVEC INITIALISATION DES PARAMÈTRES :
    // Utilisé pour initialiser une instance UserResponse avec les données d'un utilisateur.
    public UserResponse(Long id, String name, String email) {
        // ATTENTION COCO super(); // Appelle le constructeur de la classe parente (Object). Pas forcément nécessaire ici.
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
