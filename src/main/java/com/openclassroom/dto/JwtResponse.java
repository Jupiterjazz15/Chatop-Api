package com.openclassroom.dto;


// DTO (Data Transfer Object) : classe utilisée pr encapsuler la réponse contenant le token JWT envoyé au client après une authentification réussie
// Elle ne doit faire qu'une seule chose : transporter les données du token

public class JwtResponse {
    // déclaration et initialisation de token
    private String token;

    // constructeur
    public JwtResponse(String accessToken) {
        // accessToken = la chaîne de caractères représentant le token JWT généré après une authentification réussie
            //  généré par generateJwtToken du JwtUtils
        this.token = accessToken;
    }

    // GETTER ET SETTER
    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}