package com.openclassroom.controllers;

import com.openclassroom.models.Rental;
import com.openclassroom.services.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @GetMapping("")
    public ResponseEntity<List<Rental>> getAllRentals() {
        List<Rental> rentals = rentalService.getAllRentals();
        return ResponseEntity.ok(rentals); // Retourne un statut 200 avec la liste des locations
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rental> getRentalById(@PathVariable Long id) {
        Rental rental = rentalService.getRentalById(id);
        return ResponseEntity.ok(rental); // Retourne un statut 200 avec les détails de la location
    }

    // CREATION D'UN RENTAL
    @PostMapping("/{id}")
    public ResponseEntity<Rental> createRental(@RequestBody Rental rental) {
        Rental newRental = rentalService.createRental(rental);
        return ResponseEntity.ok(newRental); // Retourne un statut 200 avec la location créée
    }

    // MAJ UN RENTAL
    @PutMapping("/{id}")
    public ResponseEntity<Rental> updateRental(@PathVariable Long id, @RequestBody Rental updatedRental) {
        Rental rental = rentalService.updateRental(id, updatedRental);
        return ResponseEntity.ok(rental); // Retourne un statut 200 avec les détails de la location mise à jour
    }
}
