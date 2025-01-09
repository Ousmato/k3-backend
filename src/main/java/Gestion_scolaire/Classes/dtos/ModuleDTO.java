package Gestion_scolaire.Classes.dtos;

import Gestion_scolaire.Classes.entity.Modules;
import Gestion_scolaire.Classes.entity.UE;
import lombok.Data;

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
