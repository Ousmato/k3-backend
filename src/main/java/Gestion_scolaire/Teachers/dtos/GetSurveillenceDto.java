package Gestion_scolaire.Teachers.dtos;

import lombok.Data;

import java.time.LocalDate;
@Data
public class GetSurveillenceDto {
    private long id;
    private String nom;
    private String prenom;
    private LocalDate date;


//    public static GetSurveillenceDto toDto(Surveillence surveillence) {
//        GetSurveillenceDto dto = new GetSurveillenceDto();
//        dto.setDate(surveillence.getDate());
//        dto.setId(surveillence.getId());
//        dto.setNom(surveillence.getIdSurveillant().getNom() == null ? surveillence.getIdTeacher().getNom() : "" );
//        return dto;
//    }
}
