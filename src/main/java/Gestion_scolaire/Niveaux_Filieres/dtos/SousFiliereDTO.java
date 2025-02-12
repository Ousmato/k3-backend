package Gestion_scolaire.Niveaux_Filieres.dtos;

import Gestion_scolaire.students.dtos.GetInscriptionDto;
import Gestion_scolaire.students.dtos.InscriptionNoteDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SousFiliereDTO {
    private  long id;
    @JsonProperty("sname")
    private String name;

    private List<GetInscriptionDto> inscriptions;
}
