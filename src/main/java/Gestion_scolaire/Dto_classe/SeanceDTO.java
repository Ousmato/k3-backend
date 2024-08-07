package Gestion_scolaire.Dto_classe;

import Gestion_scolaire.Models.Seances;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class SeanceDTO {
    private long id;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private LocalDate date;
    private ModuleDTO idModule;
    private EmploisDTO idEmplois;
    private TeacherDTO idTeacher;
    private SallesDTO idSalle;
    private LocalTime debut_pause;
    private LocalTime fin_pause;

    public static SeanceDTO toSeanceDTO(Seances seance) {
        SeanceDTO dto = new SeanceDTO();
        dto.setId(seance.getId());
        dto.setHeureDebut(seance.getHeureDebut());
        dto.setHeureFin(seance.getHeureFin());
        dto.setDate(seance.getDate());
        dto.setIdTeacher(TeacherDTO.toTeacherDTO(seance.getIdTeacher()));
        dto.setIdEmplois(EmploisDTO.toEmploisDTO(seance.getIdEmplois()));
        dto.setIdModule(ModuleDTO.toModuleDTO(seance.getIdModule()));
        dto.setIdSalle(SallesDTO.toSallesDTO(seance.getIdSalle()));
        return dto;
    }
}
