package com.chatop.api.security;

import org.springframework.context.annotation.Configuration;
// @Configuration = cette classe contient des configurations Spring.
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
// @EnableMethodSecurity = active la sécurité basée sur des annotations au niveau des méthodes.
import org.springframework.beans.factory.annotation.Autowired;
// @Autowired = injecte les dépendances nécessaires
import org.springframework.context.annotation.Bean;
// @Bean = permet de déclarer des beans Spring pour la configuration.

import com.openclassroom.security.services.UserDetailsServicesImpl;
// Instance avec le détails des utilisateurs nécessaires à l'authentification
// Instance qui gère les erreurs d'accès non autorisé.
import com.openclassroom.security.jwt.AuthTokenFilter;
// Instance de filtre pr gérer les tokens JWT dans chaque requête.

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
// Fournit un mécanisme pr authentifier les users en utilisant un DAO et un encodeur de mot de passe.
import org.springframework.security.crypto.password.PasswordEncoder;
// Interface pour encoder et vérifier les mots de passe.


import org.springframework.security.authentication.AuthenticationManager;
// Gère l'authentification des utilisateurs.
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
// Permet de configurer l'authentification Spring Security.

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// Implémente un encodeur pour sécuriser les mots de passe avec l'algorithme BCrypt.

import org.springframework.security.web.SecurityFilterChain;
// Définit la chaîne des filtres de sécurité pour Spring Security.
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// Permet de configurer la sécurité web, comme les filtres et les stratégies d'accès.
import org.springframework.security.config.http.SessionCreationPolicy;
// Définit la politique de gestion des sessions, comme STATELESS.
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// Filtre utilisé pour gérer l'authentification basée sur le nom d'utilisateur et le mot de passe.

@Configuration
@EnableMethodSecurity
// classe de configuration pour gérer la sécurité de l'application.
public class WebSecurityConfig {

    // injection du service
    @Autowired
    UserDetailsServicesImpl userDetailsService;

    // injection de la classe AuthEntryPointJwt pr gérer les erreurs d'accès non autorisé.
    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    // Déclare un bean responsable du traitement des tokens JWT dans chaque requête.
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    // Déclare un bean de DaoAuthenticationProvider pr authentifier les utilisateurs en utilisant notre service et un encodeur de mdp (classe de SSECU)
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {

        // création d'une instance authProvider de type DaoAuthenticationProvider
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        // configure authProvider pr que le service utilisé pr récupérer les détails des utilisateurs est userDetailsService
        authProvider.setUserDetailsService(userDetailsService);
        // configure authProvider pr que le passwordEncoder utilisé pr encoder est passwordEncoder()
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
    // Déclare un bean AuthenticationManager est responsable de gérer le processus d'authentification des utilisateur (classe de SSECU)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    // Déclare un bean AuthenticationManager pour encoder le mdp
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
                // Désactive la protection CSRF (Cross-Site Request Forgery),
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                // Définit un gestionnaire personnalisé pour les exceptions d'authentification.
                // unauthorizedHandler est une instance d'AuthEntryPointJwt qui gère les erreurs d'accès non autorisé.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and())
                //  une gestion de session "sans état" (STATELESS) cad que chaque requête doit inclure les infos  nécessaires à l'authentification
                // (comme un token JWT) car aucune session côté serveur n'est maintenue.
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/auth/**").permitAll()
                                .anyRequest().authenticated()
                );

        http.authenticationProvider(authenticationProvider());
        //  Ajoute un fournisseur d'authentification personnalisé (codé plus haut)

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        // Ajoute un filtre personnalisé (AuthTokenFilter) avant le filtre par défaut UsernamePasswordAuthenticationFilter.
        // Ce filtre traite les tokens JWT

        return http.build();
        // Construit et retourne l'instance de SecurityFilterChain configurée.
    }

}









}
