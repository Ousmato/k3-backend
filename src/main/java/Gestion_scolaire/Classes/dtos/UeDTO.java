package Gestion_scolaire.Classes.dtos;

import Gestion_scolaire.Classes.entity.UE;
import lombok.Data;

import java.util.List;

@Data
public class UeDTO {
    private long id;
    private String code;
    private String nomUE;
    private List<ModuleDTO> modules;

    public static UeDTO toUeDTO(UE ue) {
        UeDTO dto = new UeDTO();
        dto.setCode(ue.getCodeUE());
        dto.setId(ue.getId());
        dto.setNomUE(ue.getNomUE());
        return dto;
    }
}
