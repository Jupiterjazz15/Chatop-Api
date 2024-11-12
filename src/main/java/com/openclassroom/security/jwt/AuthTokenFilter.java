package com.openclassroom.security.jwt;
// Ces importations incluent des classes Java standard et des classes Servlet, nécessaires
// pour intercepter et traiter les requêtes HTTP et gérer les exceptions.
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//permet d'indiquer que cette classe est un bean Spring, ce qui permet à Spring de la détecter
// et de l’injecter automatiquement.
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
//permet de stocker les infos d'authentification de l'utilisateur dans le contexte de sécurité de Spring.
import org.springframework.security.core.context.SecurityContextHolder;
// classe abstraite de Spring qui garantit que le filtre est appliqué une seule fois par requête
import org.springframework.web.filter.OncePerRequestFilter;

@Component
// Le filtre sera exécuté pour chaque requête afin de vérifier si un token JWT valide est présent.
public class AuthTokenFilter extends OncePerRequestFilter {
    // une dépendance qui fournit des méthodes pour valider le token JWT.
    // Ce champ est défini comme final car il est injecté par le constructeur et ne doit pas être modifié par la suite.
    private final JwtUtils jwtUtils;
    //Ce constructeur permet d'injecter une instance de JwtUtils dans AuthTokenFilter.
    // Spring injectera automatiquement cette dépendance
    public AuthTokenFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    // rédéfinition de la méthode doFilterInternal qui vient de la classe OncePerRequestFilter de SW
    // prend en paramètre la requête HTTP entrante, la sortante; le chaine def- filtres
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
            if (jwtUtils.validateJwtToken(token)) {
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
