package Gestion_scolaire.Dto_classe;

import Gestion_scolaire.EnumClasse.Seance_type;
import Gestion_scolaire.Models.Journee;
import Gestion_scolaire.Models.Participant;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class Journee_DTO {

    private long id;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private LocalDate date;
    //    private ModuleDTO idModule;
    private EmploisDTO idEmplois;
    private SallesDTO idSalle;
    private Participant idParticipant;
    private TeacherDTO idTeacher;
    private Seance_type seanceType;
    private List<String> plageHoraire;

    public static Journee_DTO toJourneeDTO(Journee seance) {
        Journee_DTO dto = new Journee_DTO();
        dto.setId(seance.getId());
        dto.setHeureDebut(seance.getHeureDebut());
        dto.setHeureFin(seance.getHeureFin());
        dto.setIdSalle(SallesDTO.toSallesDTO(seance.getIdSalle()));
        dto.setDate(seance.getDate());
        dto.setSeanceType(seance.getSeanceType());
        dto.setIdParticipant(seance.getIdParticipant());
        dto.setIdEmplois(EmploisDTO.toEmploisDTO(seance.getIdEmplois()));
        dto.setIdTeacher(TeacherDTO.toTeacherDTO(seance.getIdTeacher()));
        return dto;
    }
}
