package Gestion_scolaire.Dto_classe;

import Gestion_scolaire.Models.Modules;
import Gestion_scolaire.Models.UE;
import lombok.Data;

import java.util.List;

@Data
public class ModuleDTO {
    private long id;
    private String nomModule;
    private int coefficient;
    private double noteModule;
    private UE idUe;


    public static ModuleDTO toModuleDTO(Modules module) {
        ModuleDTO dto = new ModuleDTO();
        dto.setId(module.getId());
        dto.setNomModule(module.getNomModule());
        dto.setCoefficient(module.getCoefficient());
        dto.setIdUe(module.getIdUe());
        return dto;
    }
}
