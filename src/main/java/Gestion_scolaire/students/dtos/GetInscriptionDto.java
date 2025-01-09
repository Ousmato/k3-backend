package Gestion_scolaire.students.dtos;

import Gestion_scolaire.Administrators.entity.Admin;
import Gestion_scolaire.students.entity.Inscription;
import Gestion_scolaire.students.entity.Students;
import Gestion_scolaire.students.entity.StudentsClasse;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GetInscriptionDto {

    private long id;
    private StudentsClasse idClasse;
    private Admin idAdmin;
    private Student_DTO idEtudiant;
    private LocalDate date;

    private boolean active = true;

    private boolean payer = false;

    private double scolarite = 0;

    public static GetInscriptionDto toDto(Inscription inscription) {
        GetInscriptionDto dto = new GetInscriptionDto();
        dto.setId(inscription.getId());
        dto.setDate(inscription.getDate());
        dto.setActive(inscription.isActive());
        dto.setPayer(inscription.isPayer());
//        dto.setScolarite(inscription.getScolarite());
        dto.setIdClasse(inscription.getIdClasse());
        return dto;

    }
}
