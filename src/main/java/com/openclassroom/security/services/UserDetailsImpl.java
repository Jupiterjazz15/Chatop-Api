package com.openclassroom.security.services.;

import org.springframework.security.core.userdetails.UserDetails;
// une interface de SSECU pr encapsuler les infos utilisateur nécessaires à l'authentification.
import com.fasterxml.jackson.annotation.JsonIgnore;
//  @JsonIgnore annotation Jackson pr exclure certains champs lors de la sérialisation JSON.
import com.openclassroom.models.User;

import java.util.Objects;
// classe utilitaire pour comparer et manipuler des objets.
import java.util.Collection;
// permet l'utilisation du type de données Collection et notamment List (collection ordonnée d'élément dont on connaît le nbr )
import org.springframework.security.core.GrantedAuthority;
// représentent pour SSecu, les permissions/roles d’un utilisateur

public class UserDetailsImpl implements UserDetails {
    // décalration des attributs privés
    private Long id;
    private String username;
    private String email;
    @JsonIgnore// pr exclure certains champs dans les réponses JSON (évite exposer le mdp)
    private String password;

    // CONSTRUCTEUR pr construire un user avec les infos fournies
    public UserDetailsImpl(Long id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // MÉTHODE STATIQUE : crée une instance de UserDetailsImpl à partir d'un objet User.
    // Avec `static`, cette mthd peut être appelée sans avoir à instancier sans avoir à instancier un objet UserDetailsImpl au préalable
    public static UserDetailsImpl build(User user) {

        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword()
        );
    }

    // GETTERS PERSONNALISER
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    // GETTERS IMPLÉMENTÉS DE L'INTERFACE USERDETAILS
    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public String getUsername() {
        return username;
    }

    // MTHDS DE STATUT UTILISATEUR DE L'INTERFACE USERDETAILS
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // MTHDS SPECIFIQUES
    @Override
    public boolean isEnabled() {
        return true;
    }
    // Les mthds qui overrident héritent toutes de la classe Object "o" est un choix
    // Vérifie si 2 objets UserDetailsImpl sont égaux
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
    // Retourne les permissions (non implémenté dans ce code).
    // ATTENTION COCO
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO Auto-generated method stub
        return null;
    }
}
