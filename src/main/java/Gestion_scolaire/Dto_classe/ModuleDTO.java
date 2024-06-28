package Gestion_scolaire.Dto_classe;

import Gestion_scolaire.Models.Modules;
import lombok.Data;

@Data
public class ModuleDTO {
//    private long id;
    private String nomModule;
//    private int coefficient;
//    private UeDTO idUe;

    public static ModuleDTO toModuleDTO(Modules module) {
        ModuleDTO dto = new ModuleDTO();
//        dto.setId(module.getId());
        dto.setNomModule(module.getNomModule());
//        dto.setCoefficient(module.getCoefficient());
//        dto.setIdUe(toUeDTO(module.getIdUe()));
        return dto;
    }
}
