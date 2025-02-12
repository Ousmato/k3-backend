package Gestion_scolaire.students.dtos;

import Gestion_scolaire.Dto_classe.UeValidateDTO;
import Gestion_scolaire.students.entity.Inscription;
import Gestion_scolaire.students.entity.StudentsClasse;
import lombok.Data;

import java.util.List;

@Data
public class InscriptionNoteDTO {
    private long id;
    private StudentsClasse idClasse;
    private String nom;
    private String prenom;
    private String lieuNaissance;
    private boolean active = true;
    private String sexe;
    private String dateNaissance;
    private List<UeValidateDTO> ueValidate;


    public static InscriptionNoteDTO toDTO(Inscription inscription) {
        InscriptionNoteDTO inscriptionDTO = new InscriptionNoteDTO();
        inscriptionDTO.setId(inscription.getId());
        inscriptionDTO.setNom(inscription.getIdEtudiant().getNom());
        inscriptionDTO.setActive(inscription.isActive());
        inscriptionDTO.setPrenom(inscription.getIdEtudiant().getPrenom());
        inscriptionDTO.setIdClasse(inscription.getIdClasse());
        inscriptionDTO.setDateNaissance(inscription.getIdEtudiant().getDateNaissance());
        inscriptionDTO.setSexe(inscription.getIdEtudiant().getSexe());
        inscriptionDTO.setLieuNaissance(inscription.getIdEtudiant().getLieuNaissance());

        return inscriptionDTO;
    }
}
