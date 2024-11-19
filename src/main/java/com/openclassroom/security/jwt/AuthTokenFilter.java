package com.openclassroom.security.jwt;

import org.springframework.web.filter.OncePerRequestFilter;
// Classe de Spring qui garantit qu'un filtre est exécuté une seule fois par requête HTTP.

import org.springframework.beans.factory.annotation.Autowired;
// Utilisé pour injecter automatiquement les dépendances Spring (@Autowired)
import com.openclassroom.security.jwt.JwtUtils;
// Contient les utilitaires liés aux JSON Web Tokens (JWT), comme la validation ou l'extraction d'informations.
import com.openclassroom.security.services.UserDetailsServicesImpl;
// Service Spring permettant de récupérer les informations utilisateur pour l'authentification.
import org.slf4j.Logger;
// Définir un logger pour consigner les messages.
import org.slf4j.LoggerFactory;
// Fournit une méthode statique pour obtenir une instance de Logger.

import jakarta.servlet.http.HttpServletRequest;
// Représente une requête HTTP pour accéder aux informations liées à une requête.
import jakarta.servlet.http.HttpServletResponse;
// Représente une réponse HTTP pour configurer les informations renvoyées au client.
import jakarta.servlet.FilterChain;
// Représente la chaîne de filtres à travers lesquels passent les requêtes avant d'atteindre les contrôleurs.
import jakarta.servlet.ServletException;
// Exception lancée lorsqu'une erreur se produit dans un servlet.
import java.io.IOException;
// Exception lancée lorsqu'une erreur d'entrée/sortie se produit.

import org.springframework.security.core.userdetails.UserDetails;
// Interface Spring pour représenter les informations d'un utilisateur pour l'authentification.

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// Représente une authentification basée sur un nom d'utilisateur et un mot de passe.

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
// Fournit des détails supplémentaires pour l'authentification Web, comme des informations sur la requête.

import org.springframework.security.core.context.SecurityContextHolder;
// Permet d'accéder et de manipuler le contexte de sécurité actuel.

import org.springframework.util.StringUtils;
// Fournit des utilitaires pour travailler avec des chaînes, comme vérifier si elles ne sont pas vides.


// BUT : vérifier si une requête contient un token JWT valide + authentifier le user correspondant en configurant le SecurityContextHolder
public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;
    // injection jwtUtils

    @Autowired
    private UserDetailsServicesImpl userDetailsService;
    // injection userDetailsServiceImpl

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);
    // déclaration du logger

    // Surcharge de la mthd doFilterInternal
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            // utilisation de la mthd privée parseJwt plus bas
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                // utilisation de la mthd validateJwtToken de JwtUtils
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                //  utilisation de la mthd getUserNameFromJwtToken de JwtUtils qui retourne le mail
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                // utilisation du UserDetailsServiceImpl pr récupérer l'instance de UserDetails depuis la DB, en utilisant le email extrait.

                // Création d'un objet Authentication
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,// infos user trouvées avant
                                null,// pas de credential nécessaire, le token suffit
                                userDetails.getAuthorities());// Les rôles ou permissions du user
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Ajout de détails à l'objet authentification (infos spécifiques à la requête HTTP : l'adresse IP ou l'agent utilisateur etc.)

                SecurityContextHolder.getContext().setAuthentication(authentication);
                // Définit l'objet authentication dans le SecurityContextHolder, ce qui permet à Spring Security de reconnaître ce user comme authentifié pour cette requête.
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }

        filterChain.doFilter(request, response);
        // Passe la requête et la réponse au filtre suivant dans la chaîne, ou directement au contrôleur si c'est le dernier filtre.
    }

    // Mthd public d'extraction du token depuis les en-têtes "Authorization"  de la requête HTTP.
    public String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}
