package Gestion_scolaire.Dto_classe;

import Gestion_scolaire.Models.NiveauFilieres;
import lombok.Data;

@Data
public class NivauFilierDTO {
    private FiliereDTO idFiliere;
    private NiveauDTO idNiveau;

    public static NivauFilierDTO toNivFiliereDTO(NiveauFilieres niveauFilieres){
        NivauFilierDTO dto = new NivauFilierDTO();
        dto.setIdFiliere(FiliereDTO.toFiliereDTO(niveauFilieres.getIdFiliere()));
        dto.setIdNiveau(NiveauDTO.toNiveauDTO(niveauFilieres.getIdNiveau()));
        return dto;
    }
}
