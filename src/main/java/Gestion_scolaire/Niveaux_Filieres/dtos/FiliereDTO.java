package Gestion_scolaire.Niveaux_Filieres.dtos;

import Gestion_scolaire.Niveaux_Filieres.entity.Filiere;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class FiliereDTO {
//    private Long id;
    @JsonProperty("name")
    private String nomFiliere;
    @JsonProperty("sous")
    private List<SousFiliereDTO> sous;

    public static FiliereDTO toFiliereDTO(Filiere filiere) {
        FiliereDTO dto = new FiliereDTO();
//        dto.setId(filiere.getId());
        dto.setNomFiliere(filiere.getNomFiliere());
        return dto;
    }

}
