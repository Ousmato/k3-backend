package Gestion_scolaire.Dto_classe;

import Gestion_scolaire.Models.Emplois;
import Gestion_scolaire.Models.Seances;
import Gestion_scolaire.Models.Semestres;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class EmploisDTO {
    private long id;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private ModuleDTO idModule;
    private Semestres idSemestre;
    private ClasseDTO idClasse;
    private List<Seances> seances;

    public static EmploisDTO toEmploisDTO(Emplois emplois){
        EmploisDTO dto = new EmploisDTO();
        dto.setId(emplois.getId());
        dto.setDateDebut(emplois.getDateDebut());
        dto.setDateFin(emplois.getDateFin());
        dto.setIdModule(ModuleDTO.toModuleDTO(emplois.getIdModule()));
        dto.setIdClasse(ClasseDTO.toClasseDTO(emplois.getIdClasse()));
        return dto;
    }
}
