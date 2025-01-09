package Gestion_scolaire.Classes.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ModuleJsonDto {

    @JsonProperty("ECUE")
    private String ecue;

    @JsonProperty("Credits")
    private int credits;

    @JsonProperty("Objectif")
    private String objectif;

}
