package com.openclassroom.security.jwt;

import org.springframework.web.filter.OncePerRequestFilter; // Classe de Spring qui garantit qu'un filtre est exécuté une seule fois par requête HTTP.
import org.springframework.beans.factory.annotation.Autowired;// Utilisé pour injecter automatiquement les dépendances Spring
import com.openclassroom.security.services.UserDetailsServicesImpl;// Service Spring permettant de récupérer les infos user pour l'authentification.
import org.slf4j.Logger;// Définir un logger pour consigner les messages.
import org.slf4j.LoggerFactory; // Fournit une méthode statique pr obtenir une instance de Logger.

import jakarta.servlet.http.HttpServletRequest; // Représente une requête HTTP reçue et fournit des mthds pr accéder à ses infos (paramètres, en-têtes, etc.).
import jakarta.servlet.http.HttpServletResponse;// Représente une réponse HTTP pour configurer les informations renvoyées au client.
import jakarta.servlet.FilterChain;// Représente la chaîne de filtres à travers lesquels passent les requêtes avant d'atteindre les contrôleurs.

import jakarta.servlet.ServletException;// Exception lancée lorsqu'une erreur se produit dans un servlet.
import java.io.IOException; // Exception lancée lorsqu'une erreur d'entrée/sortie se produit.

import org.springframework.security.core.userdetails.UserDetails;// Interface Spring représentant les infos d'un user pour l'authentification.
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;// Représente une authentification basée sur un nom et un mdp.
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource; // Fournit des détails supplémentaires pr l'authentification Web, comme des infos sur la requête.
import org.springframework.security.core.context.SecurityContextHolder;// Permet d'accéder et de manipuler le contexte de sécurité actuel.
import org.springframework.util.StringUtils;// Fournit des utilitaires pr travailler avec des chaînes de caract, comme vérifier si elles ne sont pas vides.


// BUT : VERIFIER SI UNE REQUETE CONTIENT UN TOKEN JWT VALIDE + CREER L'OBJET AUTHENTICATION + L'ENREGISTRER DANS LE SECURITYCONTEXTHOLDER
public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils; // injection jwtUtils
    @Autowired
    private UserDetailsServicesImpl userDetailsService; // injection userDetailsServiceImpl

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);// déclaration du logger


    @Override // MTHD DE CREATION DE FILTRE
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);// utilisation de la mthd parseJwt plus bas qui extrait le token
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                // utilisation de la mthd validateJwtToken de JwtUtils
                String useremail = jwtUtils.getUserNameFromJwtToken(jwt);
                //  utilisation de la mthd getUserNameFromJwtToken de JwtUtils qui retourne le mail
                UserDetails userDetails = userDetailsService.loadUserByUsername(useremail);
                // utilisation du UserDetailsServiceImpl pr récupérer l'instance de UserDetails depuis la DB, en utilisant le email extrait.

                // Création d'un objet Authentication cad un user authentifié
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,// l'instance userDetails trouvée avant
                                null,// pas de credential nécessaire, le token suffit
                                userDetails.getAuthorities());// Les rôles ou permissions du user
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Ajout des détails à l'objet authentification (infos spécifiques à la requête HTTP : l'adresse IP ou l'agent utilisateur etc.)

                SecurityContextHolder.getContext().setAuthentication(authentication);
                // Définit l'objet authentication dans le SecurityContextHolder, pr que SSECU reconnaisse ce user comme authentifié pour cette requête.
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }

        filterChain.doFilter(request, response);
        // Passe la requête et la réponse au filtre suivant dans la chaîne, ou directement au contrôleur si c'est le dernier filtre.
    }

    // EXTRAIRE UN TOKEN DEPUIS L'EN-TETE "AUTHORIZATION" DE LA REQUETE HTTP ET VERIFIE SON FORMAT
    public String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}
