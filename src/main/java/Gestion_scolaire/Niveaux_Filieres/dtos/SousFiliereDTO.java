package Gestion_scolaire.Niveaux_Filieres.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SousFiliereDTO {

    @JsonProperty("sname")
    private String name;
}
