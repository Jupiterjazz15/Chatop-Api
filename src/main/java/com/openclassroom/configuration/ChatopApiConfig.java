package com.openclassroom.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ChatopApiConfig {

    //La chaîne de filtre
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Désactivation de la protection CSRF (utile pour les API avec les JWT) en utilisant l'attribut http
        return http.csrf(csrf -> csrf.disable())
                // Configuration du mode stateless : cela signifie que chaque requête est indépendante
                // et aucune session n'est maintenue entre les requêtes
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Exige que toutes les requêtes soient authentifiées, sans exception
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                // Configuration de l'authentification via OAuth2 avec JWT pour sécuriser l'accès aux ressources
                // (utilise les paramètres par défaut pour la gestion des tokens JWT)
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()))
                // Activation de l'authentification HTTP basique (souvent utilisée pour les tests ou les clients automatisés)
                .httpBasic(Customizer.withDefaults())
                // Appel de la mthd build pour construire la chaîne
                .build();
    }

    @Bean
   // utilisation de l'algorithme BCrypt pour sécuriser le mdp
    public BCryptPasswordEncoder () {
        return new BCryptPasswordEncoder();
    }

    @Bean
    //création  d'un utilisateur qui est ensuite ajouté à un gestionnaire d'utilisateurs en mémoire.
    public UserDetailsService users() {
        UserDetails user = User.builder()
                .username("usertest")
                .password(passwordEncoder().encode("passwordtest"))
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}
