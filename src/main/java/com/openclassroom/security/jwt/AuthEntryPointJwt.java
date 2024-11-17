package com.openclassroom.security.jwt;

import org.springframework.stereotype.Component;
// @Component => marquer la classe comme un composant Spring pr que Spring puisse la détecter et la gérer automatiquement
import org.springframework.security.web.AuthenticationEntryPoint;
// interface utiliser pr gérer les erreurs d'authentification dans Spring Security

import org.slf4j.Logger;
// Interface permettant de définir un logger pr consigner des messages de journalisation
import org.slf4j.LoggerFactory;
// Fournit une mthd statique pr obtenir une instance de Logger / pr identifier facilement l'origine des messages dans les journaux.

import jakarta.servlet.http.HttpServletRequest;
// représente une requête HTTP / permet d'accéder à des infos  liées à la requête ( l'URL, en-têtes, ou paramètres)
import jakarta.servlet.http.HttpServletResponse;
// représente une réponse HTTP / permet de configurer la réponse à renvoyer au clt : code de statut, contenu ou en-têtes.
import jakarta.servlet.ServletException;
//exception déclenchée qd une erreur se produit dans un servlet
import org.springframework.security.core.AuthenticationException;
// représente les types de contenu HTTP (utilisé pr définir que la réponse sera au format JSON.)
import java.io.IOException;
// exception déclenchée lorsqu'une erreur d'entrée/sortie se produit
import org.springframework.http.MediaType;

import java.util.Map;
// une collection clé-valeur
import java.util.HashMap;
// implémentation de l'interface Map qui stocke les données dans une structure clé-valeur non ordonnée.
// utilise pour créer un objet Map avec les données à inclure dans la réponse JSON.
import com.fasterxml.jackson.databind.ObjectMapper;
// Fournit des outils pr convertir des objets Java en JSON et vice-versa.
// Ici, on va convertir le Map avec les détails de l'erreur en une chaîne JSON (incluse dans la réponse HTTP)

// BUT DE CETTE CLASSE : personnaliser le msg quand un utilisateur n'a pas le droit d'accéder à une ressource
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
    // interface AuthenticationEntryPoint utilisée pr gérer les erreurs d'authentification (qd un user tente d'accéder à une ressource protégée sans être authentifié)
    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);
    // déclaration d'un logger que le associe à la classe AuthEntryPointJwt

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        // surcharge de la mthd commence appelée automatiquement par SSecu qd un user non authentifié tente d'accéder à une ressource protégée.
        logger.error("Unauthorized error: {}", authException.getMessage());
        // journalisation de l'erreur pr enregistrer un msg d'erreur avec les détails de l'exception.
        // on récupère le message associé à l'exception

        // configuration de la réponse HTTP
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        // définit que la réponse HTTP sera du JSON
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // définit le code de statut HTTP à 401

        // construction du corps de la réponse JSON
        final Map<String, Object> body = new HashMap<>();
        // création d'une map avec
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        // le code HTTP 401
        body.put("error", "Unauthorized");
        // description
        body.put("message",authException.getMessage());
        // message

        // Sérialisation de la réponse JSON
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
        // convertit la map en JSON et l'écrit directement dans le flux de sortie de la réponse
    }

}
