package Gestion_scolaire.Dto_classe;

import Gestion_scolaire.Models.NiveauFilieres;
import Gestion_scolaire.Models.StudentsClasse;
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
//        dto.setScolarite(classe.getScolarite());
//        dto.setFermer(classe.isFermer());
        dto.setIdFiliere(NivauFilierDTO.toNivFiliereDTO(classe.getIdFiliere()));
        return dto;
    }
}
