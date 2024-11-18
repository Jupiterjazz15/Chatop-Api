package com.openclassroom.security.jwt;


import org.springframework.stereotype.Component;
// @Component => cette classe est un component de Spring
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//  interface pr la journalisation des erreurs / permet d'enregistrer des msgs informatifs qui peuvent ensuite être analysés pour résoudre des problèmes

import org.springframework.beans.factory.annotation.Value;
// @Value => pr injecter des propriétés définies dans application.properties (jwtSecret et jwtExpirationMs)
import org.springframework.security.core.Authentication;
// interface de SSCU. L'objet représentant l'utilisateur actuelemnt authentifié
import com.openclassroom.security.services.UserDetailsImpl;
// Ma classe perso qui représente les informations utilisateur.

import java.util.Date;
// classe reéprésentant une date
import java.security.Key;
// classe représente une clé cryptographique, utilisée pr signer ou vérifier un JWT.
// Une clé de type Key est générée à partir de votre secret (jwtSecret) pr signer les tokens.
import io.jsonwebtoken.security.Keys;
// classe pr générer et gérer des clés cryptographiques. génération faite à partir du secret codé en base64.
import io.jsonwebtoken.io.Decoders;
// classe  utilisée pr décoder des chaînes encodées, comme celles en Base64 ou Base64URL (notre secret)
import io.jsonwebtoken.*;
// inclut l'import de plusieurs classes de la bibliothèque JJWT.

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    // initialisation d'un logger, l'outil utilisé pr générer des logs (dont les erreurs d'authentification JWT)

    // INJECTION DES PROPRIETES
    @Value("${jwtSecret}")
    private String jwtSecret;
    @Value("${jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {
        // on passe en paramètre le user actuellement authentifié
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        // Créeation d'une instance userPrincipal de type UserDetailsImpl en récupérant l'objet représentant l'utilisateur actuellement connecté
        // via authentication.getPrincipal()."

       // ma clé secrète est codée en Base64 -> décode ma clé secrète via la mthd key pr faire une clé HMAC (clé cryptographique brute) -> l'algorithmes HS256 utilise la clé HMAC afin de signer et valider le token
        return Jwts.builder()
                .setSubject((userPrincipal.getEmail()))
                //  le sujet du token, ici l'adresse email de l'utilisateur.
                .setIssuedAt(new Date())
                //  date d'émission du token (actuelle).
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                // date d'expiration
                .signWith(key(), SignatureAlgorithm.HS256)
                // signe le token avec la clé générée par la méthode key() et l'algorithme HS256.
                .compact();
                // termine la construction et retourne le token sous forme de chaîne
    }

    // Mthd utilisé dans generateJwtTok
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
        // méthode génère une clé HMAC (basée sur jwtSecret) pour signer les tokens JWT.
    }
    // je donne un token et on me revonoie l'utilisateur + plutôt mettre mail
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder()
                // permet de lire et analyser un token JWT.
                .setSigningKey(key())
                //spécifie la clé secrète pour valider la signature.
                .build()
                .parseClaimsJws(token)
                // analyse et valide le token
                .getBody()
                .getSubject();
                // retourne le sujet (email) inclus dans le token
    }
    // Mthd pour valider ou non un Toke,
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}

