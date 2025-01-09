package Gestion_scolaire.Classes.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class UeReadJsonDto {

    @JsonProperty("UE") // Spécifie que ce champ correspond à "UE" dans le JSON
    private String ue;

    @JsonProperty("Code") // Spécifie que ce champ correspond à "Code" dans le JSON
    private String code;

    @JsonProperty("Semestre")
    private String semestre;

    @JsonProperty("Modules") // Spécifie que ce champ correspond à "Modules" dans le JSON
    private List<ModuleJsonDto> modules;
}
