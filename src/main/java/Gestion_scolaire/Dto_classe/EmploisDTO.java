package Gestion_scolaire.Dto_classe;

import Gestion_scolaire.Models.Emplois;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EmploisDTO {
    private long id;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private ClasseDTO idClasse;

    public static EmploisDTO toEmploisDTO(Emplois emplois){
        EmploisDTO dto = new EmploisDTO();
        dto.setId(emplois.getId());
        dto.setDateDebut(emplois.getDateDebut());
        dto.setDateFin(emplois.getDateFin());
        dto.setIdClasse(ClasseDTO.toClasseDTO(emplois.getIdClasse()));
        return dto;
    }
}
