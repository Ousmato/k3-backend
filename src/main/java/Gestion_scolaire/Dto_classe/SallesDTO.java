package Gestion_scolaire.Dto_classe;

import Gestion_scolaire.Models.Salles;
import lombok.Data;

@Data
public class SallesDTO {
    private String nom;
    private int nombrePlaces;

    public static SallesDTO toSallesDTO(Salles salles) {
        SallesDTO sallesDTO = new SallesDTO();
        sallesDTO.setNom(salles.getNom());
        sallesDTO.setNombrePlaces(salles.getNombrePlace());
        return sallesDTO;
    }
}
