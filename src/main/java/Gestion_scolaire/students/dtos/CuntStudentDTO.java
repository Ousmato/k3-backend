package Gestion_scolaire.students.dtos;

import lombok.Data;

@Data
public class CuntStudentDTO {
    private int inscrit;
    private int non_inscrit;

    public static CuntStudentDTO getCount(int inscrit, int non_inscrit) {
        CuntStudentDTO dto = new CuntStudentDTO();
        dto.setInscrit(inscrit);
        dto.setNon_inscrit(non_inscrit);
        return dto;

    }
}

