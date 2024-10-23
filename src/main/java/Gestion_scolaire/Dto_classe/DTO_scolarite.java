package Gestion_scolaire.Dto_classe;

import Gestion_scolaire.Models.Inscription;
import Gestion_scolaire.Models.Studens;
import lombok.Data;

@Data
public class DTO_scolarite {
    private double scolarite;
    private long id;

    public static DTO_scolarite update_scolarite_student(Inscription studens) {
        DTO_scolarite dto = new DTO_scolarite();
        dto.setScolarite(studens.getScolarite());
        dto.setId(studens.getId());

        return dto;
    }
}
