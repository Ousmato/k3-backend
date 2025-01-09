package Gestion_scolaire.Classes.services;

import Gestion_scolaire.Classes.dtos.UeReadJsonDto;
import Gestion_scolaire.Classes.entity.UE;
import Gestion_scolaire.Dto_classe.SallesDTO;
import Gestion_scolaire.Niveaux_Filieres.dtos.FiliereDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class JsonDataService {

    public List<UeReadJsonDto> readUeJson(String filiere, int startSemestre, int endSemestre) {
        List<UeReadJsonDto> allUes = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            for (int semestre = startSemestre; semestre <= endSemestre; semestre++) {
                // Construire le chemin du fichier JSON
                String filePath = String.format("/data/%s/%s_s%d.json", filiere, filiere, semestre);

                System.out.println("Chemin du fichier JSON : " + filePath);

                // Charger le fichier JSON
                InputStream inputStream = getClass().getResourceAsStream(filePath);

                // Vérifier si le fichier existe
                if (inputStream == null) {
                    throw new RuntimeException("Fichier JSON introuvable : " + filePath);
                }

                // Convertir le JSON en liste d'objets UE et ajouter à la liste globale
                List<UeReadJsonDto> ueList = mapper.readValue(inputStream, new TypeReference<List<UeReadJsonDto>>() {});
                allUes.addAll(ueList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la lecture des fichiers JSON pour " + filiere + ", semestres " + startSemestre + " à " + endSemestre, e);
        }

        return allUes;
    }

    public List<SallesDTO> readSalles() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = getClass().getResourceAsStream("/data/SALLE/salles.json");
            if (inputStream == null) {
                throw new RuntimeException("Fichier JSON introuvable : /data/SALLE/salles.json");
            }
            return mapper.readValue(inputStream, new TypeReference<List<SallesDTO>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la lecture du fichier JSON des salles.", e);
        }
    }

    public List<FiliereDTO> readJsonFilieres() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = getClass().getResourceAsStream("/data/FilieresNames/name.json");
            if (inputStream == null) {
                throw new RuntimeException("Fichier JSON introuvable : /data/FilieresNames/name.json");
            }
            return mapper.readValue(inputStream, new TypeReference<List<FiliereDTO>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la lecture du fichier JSON des noms des filieres.", e);
        }
    }

}
