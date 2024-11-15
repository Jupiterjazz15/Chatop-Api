package com.openclassroom.services;

import com.openclassroom.models.User;
import com.openclassroom.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String username, String email, String password) {
        // Crypte le mot de passe avec BCrypt
        String encodedPassword = passwordEncoder.encode(password);

        // Crée un nouvel utilisateur avec le mot de passe crypté
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(encodedPassword);

        // Sauvegarde l'utilisateur en base de données
        return userRepository.save(user);
    }
}
