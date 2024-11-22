package com.openclassroom.security.services;
import org.springframework.security.core.userdetails.UserDetails; // une interface de SSECU pr encapsuler les infos user nécessaires à l'authentification
import com.fasterxml.jackson.annotation.JsonIgnore; // annotation Jackson pr exclure certains champs lors de la sérialisation JSON (transfo en JSON)
import com.openclassroom.models.User; // ma classe réprésentant les caractéristiques et les données liées à un user
import java.util.Objects;// classe utilitaire pour comparer et manipuler des objets
import java.util.Collection;// permet l'utilisation du type de données Collection dont List (collection ordonnée d'élément dont on connaît le nbr)
import org.springframework.security.core.GrantedAuthority;// représentent pour SSecu, les permissions/roles d’un user

public class UserDetailsImpl implements UserDetails {
    // DECLARATION DES ATTRIBUTS PRIVES
    private Long id;
    private String username;
    private String email;
    @JsonIgnore // pr exclure certains champs dans les réponses JSON (éviter exposer le mdp)
    private String password;

    // CONSTRUCTEUR pr construire un user avec les infos fournies
    public UserDetailsImpl(Long id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // MÉTHODE STATIQUE : la mthd peut être appelée sans avoir à instancier un objet UserDetailsImpl / la créa de l'instance UserDetailsImpl se fait à partir d'un objet User
    public static UserDetailsImpl build(User user) {

        return new UserDetailsImpl(
                user.getId(),// mthd public du model User
                user.getUsername(),// mthd public du model User
                user.getEmail(),// mthd public du model User
                user.getPassword()// mthd public du model User
        );
    }

    // GETTERS PERSONNALISES (NON IMPLÉMENTÉS DE L'INTERFACE USERDETAILS)
    public Long getId() {
        return id;
    } // ATTENTION COCO : la mthd est-elle utilisée qlq part ?

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

    // MTHD DE L'INTERFACE USERDETAILS POUR CONSIDERES TS LES USERS COMME ACTIVE (pas de cas de comptes suspendus)
    @Override
    public boolean isEnabled() {
        return true;
    }

    // MTHD HERITEE DE LA CLASSE OBJET QUI VERIFIE SI DEUX USERDETAILSIMPL SONT EGAUX
    @Override
    public boolean equals(Object o) {  // Object : la superclasse Java, donc toutes les classes héritent de sa méthode equals() / "o" est un objet quelcqonque
        if (this == o) // this = l'instance USI  / on vérifie si les 2 objets pointent vers le même espace mémoire
            return true;
        if (o == null || getClass() != o.getClass()) // vérifie si les 2 objets sont du même type
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o; // convertit l'objet o en un objet de type UDI (comme ils sont égaux)
        return Objects.equals(id, user.id); // compare les valeurs des IDs des deux objets (this.id et user.id) / si ID égaux la mthd return true
    }

    // ATTENTION COCO MTHD NON CODEE = Retourne les permissions (non implémenté dans ce code).
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO Auto-generated method stub
        return null;
    }
}
