package Gestion_scolaire.Dto_classe;

import Gestion_scolaire.EnumClasse.DocType;
import Gestion_scolaire.Models.*;
import lombok.Data;

import javax.print.Doc;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class DocDTO {
    private Documents idDocument;
    private long id;
    private int telephone;
    private List<Inscription> idInscription;
    private String nom;
    private String prenom;
    private String niveau;
    private String filiere;


    public static DocDTO toDocDTO(StudentDoc doc) {
        DocDTO docDTO = new DocDTO();
        docDTO.setFiliere(doc.getIdInscription().getIdClasse().getIdFiliere().getIdFiliere().getNomFiliere());
        docDTO.setIdDocument(doc.getIdDocument());
        docDTO.setNom(doc.getIdInscription().getIdEtudiant().getNom());
        docDTO.setPrenom(doc.getIdInscription().getIdEtudiant().getPrenom());
        docDTO.setTelephone(doc.getIdInscription().getIdEtudiant().getTelephone());
        docDTO.setNiveau(doc.getIdInscription().getIdClasse().getIdFiliere().getIdNiveau().getNom());
        return docDTO;
    }
}
