package Gestion_scolaire.Classes.dtos;

import Gestion_scolaire.Classes.entity.ClasseModule;
import Gestion_scolaire.Classes.entity.Modules;
import Gestion_scolaire.Models.Semestres;
import Gestion_scolaire.Classes.entity.UE;
import lombok.Data;

import java.util.List;

@Data
public class AddUeDTO {
    private UE idUe;
    private long idClasse;
    private Semestres semestre;

    private List<Modules> modulesNotEmp;

    private List<Modules> modules;

    public static AddUeDTO getAddUeDTO(UE ue) {
        AddUeDTO addUeDTO = new AddUeDTO();
        addUeDTO.setIdUe(ue);
        addUeDTO.setSemestre(ue.getIdSemestre());
        return addUeDTO;
    }
}
