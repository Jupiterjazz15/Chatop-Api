package com.openclassroom.controller;

import com.openclassroom.dto.request.LoginRequest;
import com.openclassroom.dto.request.SignupRequest;
import com.openclassroom.dto.response.JwtResponse;
import com.openclassroom.dto.response.SignupResponse;
import com.openclassroom.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    // Endpoint pour se connecter et obtenir un token JWT
    @PostMapping("/login")
    public JwtResponse authenticateUser(@RequestBody LoginRequest loginRequest) {
        // Authentifie l'utilisateur avec les identifiants fournis
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        // Récupère les détails de l'utilisateur après authentification
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateJwtToken(userDetails.getUsername());

        // Renvoie le token dans une réponse structurée
        return new JwtResponse(token);
    }

    @PostMapping("/register")
    public SignupResponse registerUser(@RequestBody SignupRequest signupRequest) {
        // Appelle le service pour créer un nouvel utilisateur avec le mot de passe crypté
        User registeredUser = userService.registerUser(signupRequest.getUsername(), signupRequest.getEmail(), signupRequest.getPassword());

        // Retourne seulement les informations nécessaires après l'inscription
        return new SignupResponse(registeredUser.getId(), registeredUser.getUsername());
    }
}
