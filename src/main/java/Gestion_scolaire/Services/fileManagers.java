package Gestion_scolaire.Services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class fileManagers {

    private final String locationImg = "C:\\xampp\\htdocs\\StudentImg";
    private final String locationPdf = "C:\\xampp\\htdocs\\StudentPdf";

    public String saveFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("Le fichier est vide.");
        }

        // Déterminer l'extension du fichier
        String originalFilename = file.getOriginalFilename();
        String extension = "";

        if (originalFilename != null) {
            int index = originalFilename.lastIndexOf('.');
            if (index > 0) {
                extension = originalFilename.substring(index + 1).toLowerCase();
            }
        }

        // Déterminer le répertoire de destination en fonction de l'extension
        String location;
        if (extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png") || extension.equals("gif")) {
            location = locationImg; // Répertoire pour les images
        } else if (extension.equals("pdf")) {
            location = locationPdf; // Répertoire pour les PDFs
        } else {
            throw new IOException("Type de fichier non supporté : " + extension);
        }

        // Générer un nom de fichier unique
        String fileName = System.currentTimeMillis() + "_"  +  originalFilename;
        Path filePath = Paths.get(location, fileName);

        // Créer le répertoire si nécessaire
        Files.createDirectories(filePath.getParent());

        // Enregistrer le fichier sur le système de fichiers
        Files.copy(file.getInputStream(), filePath);

        return fileName;
    }
//    --------------------------------------------------update file-----------------------

    public String updateFile(MultipartFile multipartFile, String existingFilePath) throws IOException {
        if (multipartFile.isEmpty()) {
            throw new IOException("Le fichier est vide.");
        }

        // Déterminer l'extension du fichier
        String originalFilename = multipartFile.getOriginalFilename();
        String extension = "";

        if (originalFilename != null) {
            int index = originalFilename.lastIndexOf('.');
            if (index > 0) {
                extension = originalFilename.substring(index + 1).toLowerCase();
            }
        }

        // Déterminer le répertoire de destination en fonction de l'extension
        String location;
        if (extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png") || extension.equals("gif")) {
            location = locationImg; // Répertoire pour les images
        } else if (extension.equals("pdf")) {
            location = locationPdf; // Répertoire pour les PDFs
        } else {
            throw new IOException("Type de fichier non supporté : " + extension);
        }

        // Générer un nom de fichier unique
        String fileName = System.currentTimeMillis() + "_" + originalFilename;
        Path filePath = Paths.get(location, fileName);

        // Supprimer l'ancien fichier s'il existe
        if (existingFilePath != null && !existingFilePath.isEmpty()) {
            Path oldFilePath = Paths.get(existingFilePath);
            if (Files.exists(oldFilePath)) {
                Files.delete(oldFilePath);
            }
        }

        // Créer le répertoire si nécessaire
        Files.createDirectories(filePath.getParent());

        // Enregistrer le nouveau fichier sur le système de fichiers
        Files.copy(multipartFile.getInputStream(), filePath);

        return fileName;
    }

}
