package Gestion_scolaire.Dto_classe;

import Gestion_scolaire.Models.UE;
import lombok.Data;

import java.util.List;

@Data
public class UeDTO {
    private long id;
    private String nomUE;
    private List<ModuleDTO> modules;

    public static UeDTO toUeDTO(UE ue) {
        UeDTO dto = new UeDTO();
        dto.setId(ue.getId());
        dto.setNomUE(ue.getNomUE());
        return dto;
    }
}
