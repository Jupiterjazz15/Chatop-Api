package com.openclassroom.controllers;

import com.openclassroom.models.Rental;
import com.openclassroom.services.RentalService;
import com.openclassroom.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("")
    public ResponseEntity<List<Rental>> getAllRentals() {
        List<Rental> rentals = rentalService.getAllRentals();
        return ResponseEntity.ok(rentals); // Retourne un statut 200 avec la liste des locations
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rental> getRentalById(@PathVariable Long id) {
        Optional<Rental> rental = rentalService.getRentalById(id);

        if (rental.isPresent()) {
            return ResponseEntity.ok(rental.get()); // Retourne l'objet Rental si trouvé
        } else {
            return ResponseEntity.notFound().build(); // Retourne un 404 si le Rental n'existe pas
        }
    }

    // CREATION D'UN RENTAL
    @PostMapping("/{id}")
    public ResponseEntity<Rental> createRental(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "surface") BigDecimal surface,
            @RequestParam(value = "price") BigDecimal price,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "picture", required = false) MultipartFile picture) {

        String picturePath = null;

        // Sauvegarder l'image si elle est présente
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

        // Sauvegarder le Rental
        Rental createdRental = rentalService.createRental(rental);

        // Retourner la réponse HTTP avec le Rental créé
        return ResponseEntity.ok(createdRental);
    }

    // MAJ UN RENTAL
    @PutMapping("/{id}")
    public ResponseEntity<Rental> updateRental(@PathVariable Long id, @RequestBody Rental updatedRental) {
        Rental rental = rentalService.updateRental(id, updatedRental);
        return ResponseEntity.ok(rental); // Retourne un statut 200 avec les détails de la location mise à jour
    }
}


