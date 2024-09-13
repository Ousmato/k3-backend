package Gestion_scolaire.Dto_classe;

import Gestion_scolaire.Models.Paie;
import lombok.Data;


@Data
public class PaieDTO {
    private long id;
    private long idTeacher;
    private double coutHeure;
    private int nbreHeures;
    private String date;
    private String nom;
    private String prenom;
    private String niveau;
    private String filiere;
    private String module;
    private String type;

    public static PaieDTO fromPaieDTO(Paie paie) {
        PaieDTO dto = new PaieDTO();
        dto.setId(paie.getId());
        dto.setCoutHeure(paie.getCoutHeure());
        dto.setFiliere(paie.getJournee().getIdEmplois().getIdClasse().getIdFiliere().getIdFiliere().getNomFiliere());
        dto.setNiveau(paie.getJournee().getIdEmplois().getIdClasse().getIdFiliere().getIdNiveau().getNom());
        dto.setDate(paie.getDate().toString());
        dto.setModule(paie.getJournee().getIdEmplois().getIdModule().getNomModule());
        dto.setNom(paie.getJournee().getIdTeacher().getNom());
        dto.setPrenom(paie.getJournee().getIdTeacher().getPrenom());
        dto.setType(paie.getJournee().getSeanceType().toString());
        dto.setIdTeacher(paie.getJournee().getIdTeacher().getIdEnseignant());
        return dto;
    }
}
