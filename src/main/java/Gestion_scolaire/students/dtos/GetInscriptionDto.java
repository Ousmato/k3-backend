package Gestion_scolaire.students.dtos;

import Gestion_scolaire.Administrators.entity.AdministrationUsers;
import Gestion_scolaire.Classes.dtos.StudentClasseDTO;
import Gestion_scolaire.students.entity.Inscription;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GetInscriptionDto {

    private long id;
    private StudentClasseDTO idClasse;
    private AdministrationUsers idAdministrationUsers;
    private Student_DTO idEtudiant;
    private LocalDate date;
    private boolean active = true;
    private boolean payer = false;
    private boolean totalPaie = false;

    public static GetInscriptionDto toDto(Inscription inscription) {
        GetInscriptionDto dto = new GetInscriptionDto();
        dto.setId(inscription.getId());
        dto.setDate(inscription.getDate());
        dto.setActive(inscription.isActive());
        dto.setPayer(inscription.isPayer());
        dto.setTotalPaie(inscription.isTotalPayer());
        dto.setIdClasse(StudentClasseDTO.toDto(inscription.getIdClasse()));
        return dto;
    }
}
