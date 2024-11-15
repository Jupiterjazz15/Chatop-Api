package com.openclassroom.services;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationMs}")
    private int jwtExpirationMs;

    // Méthode pour générer un token JWT
    public String generateJwtToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    // Méthode pour extraire le nom d'utilisateur du token
    public String getUsernameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Méthode pour valider le token JWT
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder() // Utilise parserBuilder() pour créer un JwtParserBuilder
                    .setSigningKey(jwtSecret) // Définit la clé de signature
                    .build() // Construit le JwtParser
                    .parseClaimsJws(token);
            return true;// Analyse le token            return true;
        } catch (JwtException | IllegalArgumentException e) {
            System.err.println("Invalid JWT token: " + e.getMessage());
        }
        return false;
    }
}
