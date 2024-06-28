package Gestion_scolaire.Dto_classe;

import Gestion_scolaire.Models.UE;
import lombok.Data;

@Data
public class UeDTO {
    private Long id;
    private String nomUE;


    public static UeDTO toUeDTO(UE ue) {
        UeDTO dto = new UeDTO();
        dto.setId(ue.getId());
        dto.setNomUE(ue.getNomUE());
        return dto;
    }
}
