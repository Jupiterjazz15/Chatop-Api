package com.openclassroom.controllers;

import com.openclassroom.models.Rental;
import com.openclassroom.models.User;
import com.openclassroom.repositories.UserRepository;
import com.openclassroom.services.RentalService;
import com.openclassroom.services.FileStorageService;
import com.openclassroom.security.jwt.AuthTokenFilter;
import com.openclassroom.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    @Autowired
    private RentalService rentalService;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private AuthTokenFilter authTokenFilter;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserRepository userRepository;


    @GetMapping("") // RECUPERER TOUTES LES LOCATIONS
    public ResponseEntity<List<Rental>> getAllRentals() {
        List<Rental> rentals = rentalService.getAllRentals();
        return ResponseEntity.ok(rentals); // Retourne un statut 200 avec la liste des locations
    }


    @GetMapping("/{id}") // RECUPERER UNE LOCATION VIA SON ID
    public ResponseEntity<Rental> getRentalById(@PathVariable Long id) {
        Optional<Rental> rental = rentalService.getRentalById(id);

        if (rental.isPresent()) {
            return ResponseEntity.ok(rental.get()); // Retourne l'objet Rental si trouvé
        } else {
            return ResponseEntity.notFound().build(); // Retourne un 404 si le Rental n'existe pas
        }
    }

    @PostMapping("") // CREER UN RENTAL
    public ResponseEntity<?> createRental(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "surface") BigDecimal surface,
            @RequestParam(value = "price") BigDecimal price,
            @RequestParam(value = "description") String description,
            @RequestParam(value = "picture") MultipartFile picture,
            @RequestParam(value = "ownerId") Long ownerId,
            HttpServletRequest request) {

        Optional<User> user = Optional.empty();

        try {
            String jwt = this.authTokenFilter.parseJwt(request); // Extraire le JWT
            if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing token.");
            }

            String userEmail = jwtUtils.getUserNameFromJwtToken(jwt); // Récupérer l'email de l'utilisateur depuis le JWT
            user = this.userRepository.findByEmail(userEmail); // Chercher l'utilisateur dans la base

            if (user.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }

            // Sauvegarder l'image si présente
            String picturePath = null;
            if (picture != null && !picture.isEmpty()) {
                picturePath = fileStorageService.saveFile(picture);
            }

            // Construire un objet Rental
            Rental rental = new Rental();
            rental.setName(name);
            rental.setSurface(surface);
            rental.setPrice(price);
            rental.setDescription(description);
            rental.setPicture(picturePath);
            rental.setOwnerId(user.get().getId()); // Associer le rental à l'utilisateur authentifié

            // Sauvegarder le Rental
            Rental createdRental = rentalService.createRental(rental);

            // Retourner le Rental créé
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRental);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + e.getMessage());
        }
    }


    @PutMapping("/{id}") // MAJ UN RENTAL
    public ResponseEntity<Rental> updateRental(@PathVariable Long id, @RequestBody Rental updatedRental) {
        Rental rental = rentalService.updateRental(id, updatedRental);
        return ResponseEntity.ok(rental); // Retourne un statut 200 avec les détails de la location mise à jour
    }
}
