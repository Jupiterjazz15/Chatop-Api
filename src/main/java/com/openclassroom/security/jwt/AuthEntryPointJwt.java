package com.openclassroom.security.jwt;

import org.springframework.stereotype.Component; // marquer la classe compo pr que Spring puisse la détecter et la gérer automatiquement
import org.springframework.security.web.AuthenticationEntryPoint; // interface utilisée pr gérer les ERREUR D AUTHENTIFICATION dans SSECU
import org.slf4j.Logger; // interface pr de définir un logger
import org.slf4j.LoggerFactory; // fournit une mthd statique pr obtenir une instance de Logger / pr identifier facilement l'origine des msgs dans les journaux.
import jakarta.servlet.http.HttpServletRequest; // requête HTTP / permet d'accéder aux infos  liées à la requête : URL, en-têtes, ou paramètres
import jakarta.servlet.http.HttpServletResponse;// réponse HTTP / permet de configurer la réponse renvoyée au clt : code de statut, contenu ou en-têtes
import jakarta.servlet.ServletException; //exception déclenchée qd une erreur se produit dans un servlet
import java.io.IOException;// exception déclenchée lorsqu'une erreur d'entrée/sortie se produit
import org.springframework.security.core.AuthenticationException;//classe fournie par SSECU pr ttes les exceptions liées à l'authentification dans l'application.
import org.springframework.http.MediaType;
import java.util.Map;// une collection clé-valeur
import java.util.HashMap; // implémentation de l'interface Map utilisé pr créer un objet Map (structure clé-valeur non ordonnée) avec les données à inclure dans la réponse JSON.
import com.fasterxml.jackson.databind.ObjectMapper;// Fournit des outils pr convertir des objets Java en JSON (convertir le Map avec détails de l'erreur en une chaîne JSON (incluse dans la réponse HTTP)

// PERSONNALISER LE MSG QUAND UN USER N'A PAS LE DROIT D'ACCEDER A UNE RESSOURCE
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint { // AuthenticationEntryPoint utilisée pr gérer les ERREUR D'AUTHENTIFICATION dans SSECU

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);
    // déclaration d'un logger que lon associe à notre classe AuthEntryPointJwt

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        // surcharge de la mthd commence appelée automatiquement par SSecu qd un user non authentifié tente d'accéder à une ressource protégée.

        logger.error("Unauthorized error: {}", authException.getMessage());
        // journalisation de l'erreur pr enregistrer un msg d'erreur et récupérer le msg associé à l'exception

        //CONFIGURATION DE LA REPONSE HTTP
        response.setContentType(MediaType.APPLICATION_JSON_VALUE); // définit que la réponse HTTP sera du JSON
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);// définit le code de statut HTTP à 401

        // CONSTRUCTION DU CORPS DE LA REPONSE EN JSON
        final Map<String, Object> body = new HashMap<>(); // création d'une map
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);// le code HTTP 401
        body.put("error", "Unauthorized");// description
        body.put("message",authException.getMessage());// message

        // SERIALISATION DE LA REPONSE EN JSON
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body); // convertit la Map en JSON et l'écrit directement dans le flux de sortie de la réponse HTTP
    }

}
