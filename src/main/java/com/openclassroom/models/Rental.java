package com.openclassroom.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity // cette classe est une entitée JPA = elle sera mappée à une table
@Table(name = "rentals")
public class Rental {

    // ATTRIBUTS PRIVES DONC UNIQUEMENT DIRECTEMENT ACCESSIBLE DANS CETTE CLASSE
    @Id // cette annotation permet à l'attibut id d'être une clé primaire
    @GeneratedValue(strategy = GenerationType.IDENTITY) //  valeur de id sera générée automatiquement via la stratégie d'incrémentation propre au SGBD
    private Integer id;

    @NotBlank // Validation au niveau de l'app d'un champ obligatoire (non null ou vide)
    @Size(max = 255) // Validation au niveau de l'app d'un longueur maximale pr le champ
    @Column(nullable = false, length = 255) // Contraintes au niveau de la DB
    private String name;

    @DecimalMin(value = "0.0", inclusive = false) // Surface doit être supérieure à 0
    @Column(nullable = false) // Champ obligatoire
    private BigDecimal surface;

    @DecimalMin(value = "0.0", inclusive = false) // Prix doit être supérieur à 0
    @Column(nullable = false) // Champ obligatoire
    private BigDecimal price;

    @Size(max = 255) // Longueur maximale définie pour l'URL de l'image
    @Column(length = 255)
    private String picture;

    @Size(max = 2000) // Description avec une longueur maximale
    @Column(length = 2000)
    private String description;

    @Column(name = "owner_id", nullable = false) // ID du propriétaire, obligatoire
    private Long ownerId;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false) // Timestamp automatique lors de la création
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at") // Timestamp automatique lors de la modification
    private LocalDateTime updatedAt;

    // CONSTRUCTEUR PAR DEFAUT (requis pour JPA)
    public Rental() {}

    // CONSTRUCTUER AVEC PARAMETRES
    public Rental(String name, BigDecimal surface, BigDecimal price, String picture, String description, Long ownerId) {
        this.name = name;
        this.surface = surface;
        this.price = price;
        this.picture = picture;
        this.description = description;
        this.ownerId = ownerId;
    }

    // GETTERS & SETTERS
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getSurface() {
        return surface;
    }

    public void setSurface(BigDecimal surface) {
        this.surface = surface;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

}