package com.openclassroom.security.services;

import org.springframework.stereotype.Service; // annotation indiquant que ce composant Spring est de type service
import org.springframework.security.core.userdetails.UserDetailsService; // Interface de SSECU utilisée pr récupérer les infos d'un user pr l'authentification
import com.openclassroom.repositories.UserRepository;// interface dont le but est de fournir les opérations nécessaires pour interagir avec DB
import org.springframework.beans.factory.annotation.Autowired; // annotation nécessaire à l'injection de dépendance
import org.springframework.security.core.userdetails.UserDetails; // interface de SSECU pr représenter les infos d'un user
import com.openclassroom.models.User;// ma classe réprésentant les caractéristiques et les données liées à un user
import org.springframework.security.core.userdetails.UsernameNotFoundException; // une classe héritant de RuntimeException de SSECU utilisée pr signaler qu'un user n'a pas été trouvé lors de la tentative de récupération de ses infos.

@Service // OVERRIDE DE LA MTHD LOADBYUSERNAME DE SSECU POUR FAIRE UNE RECHERCHE PAR MAIL
public class UserDetailsServicesImpl implements UserDetailsService {

    @Autowired  // Injection de dépendance : permet à Spring de fournir une instance de UserRepository.
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email) // cherche l'instance User existante en utilisant la mthd findByEmail() de notre UserRepository
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));
                // si instance non trouvée, une instance de UsernameNotFoundException est créée avec le msg spécifié et rien n'est retourné
        return UserDetailsImpl.build(user); // retourne une instance de UDI grâce l'instance de User trouvée. Possible grâce au constructeur statique de UDI
    }
}
