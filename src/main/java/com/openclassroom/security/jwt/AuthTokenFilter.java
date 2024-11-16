package com.openclassroom.security.jwt;
// Ces importations incluent des classes Java standard et des classes Servlet, nécessaires
// pour intercepter et traiter les requêtes HTTP et gérer les exceptions.
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import java.io.IOException;

//permet de stocker les infos d'authentification de l'utilisateur dans le contexte de sécurité de Spring.
// classe abstraite de Spring qui garantit que le filtre est appliqué une seule fois par requête
import org.springframework.web.filter.OncePerRequestFilter;

@Component
// Le filtre sera exécuté pour chaque requête afin de vérifier si un token JWT valide est présent.
public class AuthTokenFilter extends OncePerRequestFilter {
    // une dépendance qui fournit des méthodes pour valider le token JWT.
    // Ce champ est défini comme final car il est injecté par le constructeur et ne doit pas être modifié par la suite.
    private final JwtService jwtService;
    // Ajout de UserDetailsService pour charger les informations utilisateur (injecté automatiquement par Spring)
    private final UserDetailsService userDetailsService;

    // Ce constructeur permet d'injecter une instance de JwtService dans AuthTokenFilter.
    // Spring injectera automatiquement cette dépendance
    @Autowired
    public AuthTokenFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    // Redéfinition de la méthode doFilterInternal qui vient de la classe OncePerRequestFilter de SW
    // Prend en paramètre la requête HTTP entrante, la sortante; le chaine def- filtres
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        //récupère l’en-tête Authorization de la requête HTTP.
        // C’est généralement ici que le token JWT est passé sous la forme Authorization: Bearer <token>
        String authorizationHeader = request.getHeader("Authorization");

        // vérifie que l’en-tête Authorization est présent et commence par "Bearer ".
        // Cela signifie que la requête contient un token JWT sous la forme attendue
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // extrait le token JWT de l’en-tête Authorization en ignorant les 7 premiers caractères
            String token = authorizationHeader.substring(7);
            //mthd validateJwtToken de jwtUtils pour vérifier si le token est valide
            if (jwtService.validateJwtToken(token)) {
                // Extrait le nom d'utilisateur du token JWT (ajout de l’appel à getUsernameFromJwtToken)
                String username = jwtService.getUsernameFromJwtToken(token);

                // Charge les détails de l'utilisateur à partir du nom d'utilisateur extrait
                // (UserDetailsService fournit les informations complètes de l'utilisateur)
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Crée un objet UsernamePasswordAuthenticationToken pour représenter l'authentification de l'utilisateur
                // et inclut les autorités (ou rôles) de l'utilisateur
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // Définit l'authentification dans le SecurityContext de Spring pour cette requête
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        // paramètre filterChain vient du framework Servlet
        // méthode doFilter fait partie de l'interface Filter dans le framework Servlet
        // Ts les filtres d'une app web doivent implémenter cette mthd pour traiter les requêtes HTTP.
        filterChain.doFilter(request, response);
    }

    //  Ce code a pour vocation principale de récupérer le token JWT présent dans l'en-tête Authorization
    //  d'une requête HTTP, de vérifier sa validité en utilisant jwtUtils, et de passer la requête
    //  au filtre suivant dans la chaîne de filtres si le token est valide.
}
