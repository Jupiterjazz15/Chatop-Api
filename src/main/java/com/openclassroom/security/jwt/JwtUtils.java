package com.openclassroom.security.jwt;

import org.springframework.stereotype.Component; // cette classe est un component de Spring
import org.slf4j.Logger;// Définir un logger pour consigner les messages.
import org.slf4j.LoggerFactory; // Fournit une méthode statique pr obtenir une instance de Logger.

import org.springframework.beans.factory.annotation.Value;// injecter des propriétés définies dans application.properties (jwtSecret et jwtExpirationMs)
import org.springframework.security.core.Authentication;// interface de SSCU. L'objet représentant l'utilisateur actuellement authentifié
import com.openclassroom.security.services.UserDetailsImpl;// Classe perso qui représente les informations utilisateur.

import java.util.Date;// classe réprésentant une date
import java.security.Key;// classe représente une clé cryptographique, utilisée pr signer ou vérifier un JWT / elle est générée depuis ma secret (jwtSecret)
import io.jsonwebtoken.security.Keys; // classe pr générer et gérer des clés cryptographiques / génération faite à partir du secret codé en base64.
import io.jsonwebtoken.io.Decoders; // classe  utilisée pr décoder des chaînes encodées en Base64 ou Base64URL (notre secret)
import io.jsonwebtoken.*;// inclut l'import de plusieurs classes de la bibliothèque JJWT.

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    // initialisation d'un logger, l'outil utilisé pr générer des logs (dont les erreurs d'authentification JWT)

    // INJECTION DES PROPRIETES
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expirationMs}")
    private int jwtExpirationMs;

    // MTHD DE CREATION D'UN TOKEN
    public String generateJwtToken(Authentication authentication) { // Passe en paramètre le user actuellement authentifié
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        // Création d'une instance userPrincipal de type UserDetailsImpl en récupérant l'objet représentant l'utilisateur actuellement connecté
        // via authentication.getPrincipal()."

       // ma clé secrète est codée en Base64 -> décode ma clé secrète via la mthd key pr faire une clé HMAC (clé cryptographique brute) -> l'algorithmes HS256 utilise la clé HMAC afin de signer et valider le token
        return Jwts.builder()
                .setSubject((userPrincipal.getEmail())) // le sujet du token, ici l'adresse email du user
                .setIssuedAt(new Date()) // date d'émission du token (date actuelle)
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)) // date d'expiration
                .signWith(key(), SignatureAlgorithm.HS256) // signe le token avec la clé cryptogr. brute générée par la mthd key() et passe la clé dans l'algorithme HS256.
                .compact(); // termine la construction et retourne le token sous forme de chaîne
    }

    // MTHD QUI GENERE UNE CLE HMAC (cryptogr. brute générée) BASE SUR MA JWTSECRET ENCODEE EN BASE64 POUR SIGNER LE TOKEN
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    // MTHD POUR RETOURNER LE MAIL DU CLIENT A PARTIR DE SON TOKEN
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder()// permet de lire et analyser un token JWT pour le transformer de encoded à decocded
                    .setSigningKey(key())// utilise la clé HMAC pour valider la signature
                .build().parseClaimsJws(token)// analyse et valide le token
                .getBody() // une fois le token validé, on extrait les claims pr accéder à la partie playload du token
                .getSubject();// récupère la valeur associée à la clé "sub" du playload
    }

    // MTHD POUR VALIDER LE TOKEN
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

