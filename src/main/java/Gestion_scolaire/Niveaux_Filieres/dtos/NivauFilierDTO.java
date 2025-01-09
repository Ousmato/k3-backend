package Gestion_scolaire.Niveaux_Filieres.dtos;

import Gestion_scolaire.Niveaux_Filieres.entity.NiveauFilieres;
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
