package com.openclassroom.security.services;

import org.springframework.stereotype.Service;
// @Service => indiquer que c'est un composant Spring de type service
import org.springframework.security.core.userdetails.UserDetailsService;
// Interface de Spring Security utilisée pour récupérer les infos d'un utilisateur pr l'authentification
import com.openclassroom.repositories.UserRepository;
// mon UserRepository
import org.springframework.beans.factory.annotation.Autowired;
// @Autowired => nécessaire à l'injection de dépendance
import org.springframework.security.core.userdetails.UserDetails;
// Interface de Spring Security pr représenter les infos d'un utilisateur pr l'authentification
import com.openclassroom.models.User;
// mon modèle User
import org.springframework.security.core.userdetails.UsernameNotFoundException;
// une classe héritant de RuntimeException de SSECU utilisée pr signaler qu'un user n'a pas été trouvé lors de la tentative de récupération de ses infos.

@Service // class de Spring de type service
public class UserDetailsServicesImpl implements UserDetailsService {

    @Autowired  // Injection de dépendance : permet à Spring de fournir une instance de UserRepository.
    UserRepository userRepository;

    @Override // on override la mthd loadUserByUsername de SSECU pour faire une recherche par mail
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                //  on cherche une instance existante User en utilisant la méthode findByEmail(email) de notre UserRepository
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));
        // si je ne trouve pas l'instance existante de User, une instance de UsernameNotFoundException est créée avec le message spécifié
        return UserDetailsImpl.build(user);
        // On retourne une instance de UserDetailsImpl en utilisant l'instance de User trouvée précédemment. Cela est possible grâce à la mthd statique
        // de fabrication `build`, qui construit une instance de UserDetailsImpl à partir de l'objet User.
    }

}
