package Gestion_scolaire.Niveaux_Filieres.dtos;

import Gestion_scolaire.EnumClasse.Facultes;
import Gestion_scolaire.Niveaux_Filieres.entity.Filiere;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.util.List;

@Data
public class FiliereDTO {
//    private Long id;
    @JsonProperty("name")
    private String nomFiliere;

    @JsonProperty("faculte")
    @Enumerated(EnumType.STRING)
    private Facultes faculte;

    @JsonProperty("sous")
    private List<SousFiliereDTO> sous;

    public static FiliereDTO toFiliereDTO(Filiere filiere) {
        FiliereDTO dto = new FiliereDTO();
        dto.setFaculte(filiere.getFaculte());
        dto.setNomFiliere(filiere.getNomFiliere());
        return dto;
    }

}
