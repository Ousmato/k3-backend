package Gestion_scolaire.Dto_classe;

import Gestion_scolaire.EnumClasse.DocType;
import Gestion_scolaire.Models.Admin;
import Gestion_scolaire.Models.Documents;
import Gestion_scolaire.Models.Studens;
import Gestion_scolaire.Models.StudentDoc;
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
    private List<Studens> idEtudiant;
    private String nom;
    private String prenom;
    private String niveau;
    private String filiere;


    public static DocDTO toDocDTO(StudentDoc doc) {
        DocDTO docDTO = new DocDTO();
        docDTO.setFiliere(doc.getIdEtudiant().getIdClasse().getIdFiliere().getIdFiliere().getNomFiliere());
        docDTO.setIdDocument(doc.getIdDocument());
        docDTO.setNom(doc.getIdEtudiant().getNom());
        docDTO.setPrenom(doc.getIdEtudiant().getPrenom());
        docDTO.setTelephone(doc.getIdEtudiant().getTelephone());
        docDTO.setNiveau(doc.getIdEtudiant().getIdClasse().getIdFiliere().getIdNiveau().getNom());
        return docDTO;
    }
}