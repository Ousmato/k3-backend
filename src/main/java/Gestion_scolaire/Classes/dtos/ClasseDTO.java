package Gestion_scolaire.Classes.dtos;

import Gestion_scolaire.Niveaux_Filieres.dtos.NivauFilierDTO;
import Gestion_scolaire.students.entity.StudentsClasse;
import lombok.Data;

@Data
public class ClasseDTO {
    private long id;
//    private double scolarite;
//    private boolean fermer;
    private NivauFilierDTO idFiliere;

    public static ClasseDTO toClasseDTO(StudentsClasse classe) {
        ClasseDTO dto = new ClasseDTO();
        dto.setId(classe.getId());
        dto.setIdFiliere(NivauFilierDTO.toNivFiliereDTO(classe.getIdFiliere()));
        return dto;
    }
}
