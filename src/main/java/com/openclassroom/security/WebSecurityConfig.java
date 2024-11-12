package com.openclassroom.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.openclassrooms.security.jwt.AuthTokenFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final AuthTokenFilter authTokenFilter;
    // Constructeur de la class
    public WebSecurityConfig(AuthTokenFilter authTokenFilter) {
        this.authTokenFilter = authTokenFilter;
    }

    //La chaîne de filtre
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Désactivation de la protection CSRF (utile pour les API avec les JWT) en utilisant l'attribut http
        return http
                .csrf(csrf -> csrf.disable())
                // Configuration du mode stateless : cela signifie que chaque requête est indépendante
                // et aucune session n'est maintenue entre les requêtes
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Exige que toutes les requêtes soient authentifiées, sans exception
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/register").permitAll() // Routes publiques
                        .requestMatchers("/me", "/rentals").authenticated() // Routes sécurisées
                        .anyRequest().authenticated()
                        )
                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class) // Filtre JWT
                .build();
    }

    @Bean
    // utilisation de l'algorithme BCrypt pour sécuriser le mdp
    public BCryptPasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }

    @Bean
    //création d'un utilisateur qui est ensuite ajouté à un gestionnaire d'utilisateurs en mémoire.
    public UserDetailsService users() {
        UserDetails user = User.builder()
                .username("usertest")
                .password(passwordEncoder().encode("passwordtest"))
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}
