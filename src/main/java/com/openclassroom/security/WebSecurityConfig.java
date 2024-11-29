package com.openclassroom.security;

import org.springframework.context.annotation.Configuration; // Annotation indiquant que cette classe contient des configurations Spring.
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; // Annotation qu active la sécu basée sur des annotations au niveau des méthodes.
import org.springframework.beans.factory.annotation.Autowired; // Annotation d'injection de dépendances
import org.springframework.context.annotation.Bean; // Annotation pr déclarer des beans Spring pr la configuration
import com.openclassroom.security.services.UserDetailsServicesImpl; // Classe qui utilise le UserRepo pr charger les infos d'un user à partir de son email.
import com.openclassroom.security.jwt.AuthEntryPointJwt; // Classe qui personnalise le msg qd un user n'a pas le droit d'accéder à une ressource
import com.openclassroom.security.jwt.AuthTokenFilter;// Classe qui valide le token d'un requête et enregistre le authentication dans le Secu Cont. Holder
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;// Fournit un mécanisme pr authentifier les users en utilisant un DAO et un encodeur de mmdp
import org.springframework.security.crypto.password.PasswordEncoder; // Interface pour encoder et vérifier les mdp
import org.springframework.security.authentication.AuthenticationManager; // Gère l'authentication des utilisateurs.
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;// Permet de configurer l'authentication SSECU
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;// Implémente un encodeur pr sécuriser les mdp avec l'algorithme BCrypt.
import org.springframework.security.web.SecurityFilterChain; // Définit la chaîne des filtres de sécurité pour Spring Security.
import org.springframework.security.config.annotation.web.builders.HttpSecurity;// Permet de configurer la sécurité web, comme les filtres et les stratégies d'accès.
import org.springframework.security.config.http.SessionCreationPolicy; // Définit la politique de gestion des sessions comme STATELESS cad que chaque requête doit inclure les infos nécessaires à l'authentification (comme un token)
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;// Filtre utilisé pour gérer l'authentication basée sur le nom du user (mail) et le mdp

@Configuration
@EnableMethodSecurity

// CLASSE POUR POUR CONFIGURER LA SECURITE DE NOTRE APPLICATION
public class WebSecurityConfig {

    @Autowired // injection
    UserDetailsServicesImpl userDetailsService;
    @Autowired // injection
    private AuthEntryPointJwt unauthorizedHandler;


    @Bean // Définit un bean qui gère le processus d'authentification des users (classe Spring Security).
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean // Définit un bean pour encoder les mots de passe (classe Spring Security)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean // Définit un bean qui configure l'authentification (Data Access Object) : (1) passage par userDetailsService et (2) utilisation de passwordEncoder()
    public DaoAuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(); // création d'une instance authProvider de type DaoAuthenticationProvider
        authProvider.setUserDetailsService(userDetailsService); // configure authProvider pr qu'il passe par le userDetailsService pour récupérer les infos du user
        authProvider.setPasswordEncoder(passwordEncoder()); // configure authProvider pr utilisé passwordEncoder() comme encodeur

        return authProvider;
    }

    @Bean // Définit un bean qui valide les tokens JWT d'une requête et enregistre l'authentification dans le SecurityContextHolder.
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean // Déclare un bean pour appliquer une chaine de filtre à la requête
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable()) // Désactive la protection CSRF (Cross-Site Request Forgery),
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler)) // Définit un gestionnaire personnalisé pour les exceptions d'authentification.
                // unauthorizedHandler est une instance d'AuthEntryPointJwt qui gère les erreurs d'accès non autorisé.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and())
                // gestion de session STATELESS cad que chaque requête doit inclure les infos nécessaires à l'authentificatio (comme un token JWT) car aucune session côté serveur n'est maintenue.
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/retals/**").permitAll()// on peut accéder à ces routes sans authentification
                        .anyRequest().authenticated() // toutes les autres en ont besoin
                );

        http.authenticationProvider(authenticationProvider()); // Ajoute un fournisseur d'authentification personnalisé (DAO codé plus haut)

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class); // Ajoute notre filtre perso (AuthTokenFilter) avant le filtre par défaut UsernamePasswordAuthenticationFilter.

        return http.build(); // Construit et retourne l'instance de type SecurityFilterChain configurée.
    }

}


