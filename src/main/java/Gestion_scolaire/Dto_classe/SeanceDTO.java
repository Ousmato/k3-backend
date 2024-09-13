package Gestion_scolaire.Dto_classe;

import Gestion_scolaire.Models.Seances;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class SeanceDTO {
    private long id;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private LocalDate date;
//    private ModuleDTO idModule;
    private EmploisDTO idEmplois;
    private SallesDTO idSalle;
    private LocalTime pause_matin;
    private LocalTime pause_midi;
    private List<String> plageHoraire;

    public static SeanceDTO toSeanceDTO(Seances seance) {
        SeanceDTO dto = new SeanceDTO();
        dto.setId(seance.getId());
        dto.setHeureDebut(seance.getHeureDebut());
        dto.setHeureFin(seance.getHeureFin());
        dto.setDate(seance.getDate());
        dto.setIdEmplois(EmploisDTO.toEmploisDTO(seance.getIdEmplois()));
//        dto.setFin_pause(LocalTime);
        return dto;
    }
}
