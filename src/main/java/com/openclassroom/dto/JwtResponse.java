package com.openclassroom.dto;

// DTO POUR TRANSPORTER LE TOKEN UNE FOIS L'AUTHENTIFICATION REUSSIE
public class JwtResponse {

    private String token;// déclaration de l'attribut token

    // CONSTRUCTEUR
    public JwtResponse(String jwt) { // jwt = la chaîne de caractères représentant le token généré après une authentification réussi généré par generateJwtToken du JwtUtils
        this.token = jwt;
    }

    // GETTER ET SETTER
    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}