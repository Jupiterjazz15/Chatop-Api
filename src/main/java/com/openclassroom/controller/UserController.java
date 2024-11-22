package com.openclassroom.controller;


import java.util.Optional;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import com.openclassroom.dto.MessageResponse;
import com.openclassroom.dto.UserResponse;

import org.springframework.web.bind.annotation.CrossOrigin;
// @CrossOrigin : permet de configurer les permissions pour les requêtes CORS (Cross-Origin Resource Sharing),
// afin que les clients sur d'autres domaines puissent accéder à l'API.
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
// @RequestMapping : Annotation utilisée pr définir le mapping des requêtes HTTP vers des méthodes spécifiques d'un contrôleur.
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.AuthenticationManager;
import com.openclassroom.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.openclassroom.security.jwt.JwtUtils;

import java.util.HashMap;
import org.springframework.http.converter.json.MappingJacksonValue;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;

import com.openclassroom.dto.LoginRequest;
import org.springframework.security.core.Authentication;
import com.openclassroom.security.services.UserDetailsImpl;
import com.openclassroom.dto.JwtResponse;

import com.openclassroom.dto.SignupRequest;
import com.openclassroom.models.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@CrossOrigin(origins = "*", maxAge = 3600) // durée de mise en cache 1h
@RestController
@RequestMapping("/api/auth")

public class UserController {

    // injection de l'instance définit dans WebSecurityconfig
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtils jwtUtils;
    // Déclaration et initialisation de la variable responseMessage, une HashMap utilisée pour stocker des paires clé [String]-valeur [Object].
    HashMap<String, Object> responseMessage = null;
    // Déclaration et initialisation de la variable jsonResponse, un objet MappingJacksonValue utilisé avec Jackson pour appliquer des règles
    // de sérialisation spécifiques (ex. filtrage, vues) lors de la production d'une réponse JSON.
    MappingJacksonValue jsonResponse = null;

    // CONSTRUCTEUR
    public UserController() {
        this.responseMessage = new HashMap<String, Object>();
        // Création d'une nouvelle instance de HashMap pour la variable d'instance `responseMessage`.
        // Cela permet de stocker des paires clé-valeur pour préparer une réponse structurée.

        this.jsonResponse = new MappingJacksonValue(null);
        // Initialisation de la variable d'instance `jsonResponse` en créant une nouvelle instance de `MappingJacksonValue`.
        // La valeur passée au constructeur est `null`, ce qui signifie que l'objet JSON à mapper n'est pas encore défini.
    }

    @PostMapping("/login")
    // Cette méthode sera appelée lorsqu'une requête POST est envoyée à cette URL.
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest){
    // Cette méthode renvoie une réponse HTTP personnalisée (ResponseEntity).
    // Elle prend en paramètre un objet `LoginRequest` (envoyé dans le corps de la requête).
    // `@Valid` : vérifie que les champs de `loginRequest` respectent les contraintes définies (ex. : @NotBlank).
    // `@RequestBody` : convertit le corps JSON de la requête en un objet Java.

        boolean isExist = this.userRepository.existsByEmail(loginRequest.getEmail());
        // Appelle la méthode `existsByEmail` de `userRepository` pour vérifier si l'email existe.

        if (!isExist) {
            // Si l'email n'existe pas, on renvoie une réponse d'erreur.
            this.responseMessage.put("Error", "Email or Password incorrect!");
            // Ajoute un message d'erreur dans la Map `responseMessage`.

            this.responseMessage.put("status", 401);
            // Ajoute un code HTTP 401 (non autorisé) dans `responseMessage`.

            this.jsonResponse.setValue(this.responseMessage);
            // Convertit la Map en JSON grâce à `MappingJacksonValue`.

            return new ResponseEntity<Object>(this.jsonResponse, HttpStatus.UNAUTHORIZED);
            // Retourne une réponse HTTP avec le JSON et le statut 401.
        }

        // Authentifie l'utilisateur avec l'email et le mot de passe.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        // `authenticationManager.authenticate` : VERIFIE l'email et le mdp correspondent. Renvoie un objet `Authentication` contenant les infos du user  authentifié.

        SecurityContextHolder.getContext().setAuthentication(authentication);
        // STOCKE les infos du user authentifié dans le `SecurityContextHolder, ce qui rend le user disponible pour le reste du traitement.

        String jwt = jwtUtils.generateJwtToken(authentication);
        // GENERE un token JWT pour l'utilisateur authentifié à partir des info contenues dans l'objet `authentication`.

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        // RECUPERE les détails de l'utilisateur (email, id, etc.) depuis `authentication`.

        return ResponseEntity.ok(new JwtResponse(jwt));
        // RETOURNE une réponse HTTP 200 (OK) avec un objet `JwtResponse` contenant le token JWT.
    }

    @PostMapping("/register")
    // Cette méthode sera appelée lorsqu'une requête POST est envoyée à cette URL.
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    // Renvoie une réponse HTTP personnalisée (ResponseEntity).
    // Elle prend en paramètre un objet `SignupRequest` (envoyé dans le corps de la requête).
    // `@Valid` : vérifie que les champs de `signUpRequest` respectent les contraintes définies (ex. : @NotBlank).
    // `@RequestBody` : convertit le corps JSON de la requête en un objet Java.

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            // Si l'email existe déjà, on renvoie une réponse d'erreur.
            // existsByEmail vient du UserRepo et getEmail de SignUpReq
            return ResponseEntity
                    .badRequest()// Renvoie une réponse HTTP avec le statut 400 (bad request).
                    .body(new MessageResponse("Email is already used!", 400));// Renvoie un msg d'erreur dans le corps de la réponse.
        }

        // Crée une instance d'un utilisateur (nouveau user)
        User user = new User(signUpRequest.getUsername(),
                // Utilise les informations envoyées dans `signUpRequest`.
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));// Encode le mot de passe avec `encoder` (par exemple, BCrypt).

        userRepository.save(user);
        // Sauvegarde l'utilisateur dans la base de données.

        // Authentifie automatiquement l'utilisateur après son inscription en stockant les informations de l'utilisateur authentifié dans le `SecurityContextHolder`.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signUpRequest.getEmail(), signUpRequest.getPassword()));
        // Vérifie les identifiants de l'utilisateur (email et mot de passe).
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Stocke

        String jwt = jwtUtils.generateJwtToken(authentication);
        // Génère un token JWT pour l'utilisateur authentifié.

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        // Récupère les détails de l'utilisateur (email, id, etc.) depuis `authentication`.

        return ResponseEntity.ok(new JwtResponse(jwt));
        // Retourne une réponse HTTP 200 (OK) avec un objet `JwtResponse` contenant le token JWT.
    }

    @GetMapping("/me")
    // RECUPERER les informations de l'utilisateur connecté.
    public UserResponse me(HttpServletRequest request) {
    // Méthode qui renvoie un objet `UserResponse` contenant les détails de l'utilisateur connecté.
    // Prend en paramètre un `HttpServletRequest` pour accéder à l'en-tête ou au corps de la requête.

        Optional<User> user = null;
        // Déclare une variable `user` de type `Optional<User>` qui contiendra l'utilisateur récupéré depuis la base de données.
        UserResponse userResponse = null;
        // Déclare une variable `userResponse` pour stocker la réponse à renvoyer.

        try {
            // Bloque de code entouré par un `try-catch` pour gérer les exceptions qui pourraient survenir.

            String jwt = parseJwt(request);
            // Appelle la mthd  `parseJwt` du AuthTokenFilter pour extraire le JWT de la requête HTTP. Cette mthd analyse l'en-tête `Authorization` pour récupérer le token.

            String useremail = jwtUtils.getUserNameFromJwtToken(jwt);
            // Utilise le JWT pour récupérer l'email (ou autre identifiant) de l'utilisateur grâce à `getUserNameFromJwtToken`.

            user = this.userRepository.findByEmail(useremail);
            // Recherche l'utilisateur dans la base de données à l'aide de son email avec `findByEmail`.

            userResponse = new UserResponse(
                    user.get().getId(),
                    user.get().getUsername(),
                    user.get().getEmail()
            );
            // Si le user est trouvé, on initialise un objet `UserResponse` avec les détails de l'utilisateur (mthds de User)

        } catch (Exception e) {
            // Gère les exceptions, par exemple, si le JWT est invalide ou si l'utilisateur n'est pas trouvé.
            // ATTENTION COCO
        }

        return userResponse;
    }

    public String parseJwt(HttpServletRequest request) {

        String headerAuth = request.getHeader("Authorization");


        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {


            return headerAuth.substring(7);

        }

        return null;

    }

}
