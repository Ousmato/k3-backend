package Gestion_scolaire.Dto_classe;

import Gestion_scolaire.Models.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class SoutenanceDTO {

    private long id;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private LocalDate date;
    private long idAdmin;
    private List<JuryDto> idJury;
    private String filiere;
    private String niveaux;
    private Teachers idTeacher;
    private long idDoc;
    private List<Inscription> inscriptions;
    private Salles idSalle;

    public static SoutenanceDTO toDto(Soutenance soutenance) {
        SoutenanceDTO dto = new SoutenanceDTO();
        dto.setId(soutenance.getId());
        dto.setHeureDebut(soutenance.getHeureDebut());
        dto.setHeureFin(soutenance.getHeureFin());
        dto.setDate(soutenance.getDate());
        dto.setIdSalle(soutenance.getIdSalle());
        return dto;

    }
}
