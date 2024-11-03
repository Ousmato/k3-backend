package Gestion_scolaire.Dto_classe;

import Gestion_scolaire.Models.ClasseModule;
import Gestion_scolaire.Models.Modules;
import Gestion_scolaire.Models.Semestres;
import Gestion_scolaire.Models.UE;
import lombok.Data;

import java.util.List;

@Data
public class AddUeDTO {
    private UE idUe;
    private long idClasse;
    private Semestres semestre;

    private List<Modules> modules;

    public static AddUeDTO getAddUeDTO(ClasseModule clm) {
        AddUeDTO addUeDTO = new AddUeDTO();
        addUeDTO.setIdUe(clm.getIdUE());
        addUeDTO.setSemestre(clm.getIdSemestre());
        return addUeDTO;
    }
}
