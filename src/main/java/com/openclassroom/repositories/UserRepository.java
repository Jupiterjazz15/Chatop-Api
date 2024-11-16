package com.openclassroom.repositories;

import org.springframework.stereotype.Repository;
// annotation @Repository, indique que cette interface est un composant Spring pour gérer l'accès aux données.
import org.springframework.data.jpa.repository.JpaRepository;
// fournit l'interface JpaRepository incluant les mthds pr interagir avec la DB (CRUD, etc.).
import java.util.Optional;
// utilisé pr éviter les retours null et indiquer qu'une valeur peut ou non être présente
import com.openclassroom.models.User;

import org.springframework.beans.factory.annotation.Autowired;
// ATTENTION COCO : nécessaire ??
// annotation @Autowired qui permet d'injecter automatiquement des dépendances Spring
import org.springframework.data.jpa.repository.Query;
// annotation @Query utilisée pour définir des requêtes SQL personnalisées.
import org.springframework.data.repository.query.Param;
// annotation @Param utilisée pour lier les paramètres des requêtes à leurs valeurs.

@Repository // interface UserRepository gère l'accès aux données.
public interface UserRepository extends JpaRepository<User, Long> {
    // mthds de JpaRepository
    Optional<User> findByEmail(String email);
    // mthds de JpaRepository
    Optional<User> findById(long id);
    // mthds de JpaRepository pour éviter les doublons
    Boolean existsByUsername(String username);
    // mthds de JpaRepository pour éviter les doublons
    Boolean existsByEmail(String email);

    // mthds qui n'est pas dans JpaRepository donc on la créé à la main  // ATTENTION COCO : nécessaire ??
    // @Autowired // ATTENTION COCO : nécessaire ??
    // @Query(value = "SELECT * FROM users WHERE email = :email", nativeQuery = true)
    // Optional<User> findByEmail(@Param("email") String email);
}