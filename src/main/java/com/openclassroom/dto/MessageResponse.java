package com.openclassroom.dto;

// DTO POUR TRANSPORTER UNE REPONSE CONTENANT UN MSG (expl : une réponse standardisée pr les erreurs ou les succès dans les API) avec un code associé)
public class MessageResponse {

    // ATTTRIBUTS
    private String message; // Contient un message descriptif (succès, erreur, etc.)
    private int code; // Code numérique associé au message

    // CONSTRUCTEUR
    public MessageResponse(String message, int code) {
        this.message = message;
        this.code = code;
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
