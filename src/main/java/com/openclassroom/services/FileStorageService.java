package com.openclassroom.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {

    private final String uploadDir = "/absolute/path/to/uploads"; // Remplace par ton chemin absolu

    public String saveFile(MultipartFile file) {
        try {
            // Créer le répertoire d'upload s'il n'existe pas
            Files.createDirectories(Paths.get(uploadDir));

            // Générer un nom unique pour le fichier
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            // Construire le chemin complet
            Path filePath = Paths.get(uploadDir, fileName);

            // Sauvegarder le fichier
            Files.write(filePath, file.getBytes());

            // Retourner le chemin absolu
            return filePath.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file", e);
        }
    }
}
