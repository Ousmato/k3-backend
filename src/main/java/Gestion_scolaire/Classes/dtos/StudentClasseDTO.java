package Gestion_scolaire.Classes.dtos;

import Gestion_scolaire.Models.AnneeScolaire;
import Gestion_scolaire.Niveaux_Filieres.entity.NiveauFilieres;
import Gestion_scolaire.Niveaux_Filieres.entity.SousFilieres;
import Gestion_scolaire.students.entity.StudentsClasse;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class StudentClasseDTO {
    private long id;

    private AnneeScolaire idAnneeScolaire;

    private int effectifs = 0;

    private boolean fermer;

    private NiveauFilieres idFiliere;

    private List<SousFilieres> specialites;

    public static StudentClasseDTO toDto(StudentsClasse classe){
        StudentClasseDTO dto = new StudentClasseDTO();
        dto.setId(classe.getId());
        dto.setIdAnneeScolaire(classe.getIdAnneeScolaire());
        dto.setFermer(classe.isFermer());
        dto.setIdFiliere(classe.getIdFiliere());
        dto.setSpecialites(new ArrayList<>());
        return dto;
    }
}
