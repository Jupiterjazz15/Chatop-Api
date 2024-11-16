package com.openclassroom.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chatop.api.models.User;
import com.chatop.api.repository.UserRepository;


// class Springboot avec la mthd loadUserByUsername
@Service
public class UserDetailsServicesImpl implements UserDetailsService {
    // injection Userrepo
    @Autowired
    UserRepository userRepository;

    @Override // au lieu de passer par le nom on passe par le miaiil
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));
        // on peut utiliser build ici sans créer une instance de UserDetailsImpl
        return UserDetailsImpl.build(user);
    }

}

// UserDetailsImpl = le user retouvé par son mail