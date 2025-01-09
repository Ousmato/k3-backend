package Gestion_scolaire.students.dtos;

import Gestion_scolaire.Administrators.entity.Admin;
import Gestion_scolaire.students.entity.Inscription;
import Gestion_scolaire.students.entity.Students;
import Gestion_scolaire.students.entity.StudentsClasse;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class InscriptionDTO {
    private long id;
    private StudentsClasse idClasse;
    private Admin idAdmin;
    private Students idEtudiant;
//    private LocalDate date;
//
//    private boolean active = true;
//
//    private boolean payer = false;
//
//    private double scolarite = 0;
//
//    private long adminPaye;
//
//
//    public static InscriptionDTO toDTO(Inscription inscription) {
//        InscriptionDTO inscriptionDTO = new InscriptionDTO();
//        inscriptionDTO.setId(inscription.getId());
//        inscriptionDTO.setDate(inscription.getDate());
//        inscriptionDTO.setActive(inscription.isActive());
//        inscriptionDTO.setPayer(inscription.isPayer());
//        inscriptionDTO.setScolarite(inscription.getScolarite());
//        inscriptionDTO.setAdminPaye(inscription.getAdminPaye());
//        inscriptionDTO.setIdClasse(inscription.getIdClasse());
//        inscriptionDTO.setIdEtudiant(inscription.getIdEtudiant());
//
//        if (inscription.getIdEtudiant() != null && inscription.getIdEtudiant().getSeries() != null) {
//            String formattedSeries = inscription.getIdEtudiant()
//                    .getSeries()
//                    .toString()
//                    .replace("_", " ")
//                    .toUpperCase();
//            inscriptionDTO.getIdEtudiant().setSeries(formattedSeries); // Formatage appliqué
//        }
//        return inscriptionDTO;
//    }
}
