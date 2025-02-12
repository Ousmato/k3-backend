package Gestion_scolaire.students.dtos;

import lombok.Data;

@Data
public class FiliereInscritDTO {
    private long idFiliere;
    private String filiereName;
    private int numbreInscrit;
    private int numberNotInscrit;
}
