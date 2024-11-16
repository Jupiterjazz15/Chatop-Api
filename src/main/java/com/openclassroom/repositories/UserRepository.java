package com.chatop.api.repository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chatop.api.models.User;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findById(long id);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Autowired
    @Query(value = "SELECT * FROM users_table WHERE email = :email", nativeQuery = true)
    Optional<User> findByEmail(@Param("email") String email);
}