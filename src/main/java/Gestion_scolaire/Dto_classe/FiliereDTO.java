package Gestion_scolaire.Dto_classe;

import Gestion_scolaire.Models.Filiere;
import lombok.Data;

@Data
public class FiliereDTO {
//    private Long id;
    private String nomFiliere;

    public static FiliereDTO toFiliereDTO(Filiere filiere) {
        FiliereDTO dto = new FiliereDTO();
//        dto.setId(filiere.getId());
        dto.setNomFiliere(filiere.getNomFiliere());
        return dto;
    }

}
