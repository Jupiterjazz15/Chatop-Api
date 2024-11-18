package com.openclassroom.dto;

// Classe DTO (Data Transfer Object) utilisé pour transporter une réponse contenant un message
// (par exemple, une réponse standardisée pour les erreurs ou les succès dans les API) avec un code associé.

public class MessageResponse {

    // Déclaration des attributs
    private String message;
    // Contient un message descriptif (succès, erreur, etc.).
    private int code;
    // Code numérique associé au message

    // Constructeur : permet de créer une instance de MessageResponse en initialisant uniquement le message.
    public MessageResponse(String message) {
        this.message = message;
    }

    // GETTERS & SETTERS
    public int getCode() {
        return code;
    }

    public void setCode(int c) {
        this.code = c;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
