package Gestion_scolaire.students.dtos;

import Gestion_scolaire.Models.AnneeScolaire;
import lombok.Data;

import java.util.List;

@Data
public class StatistiqueDTO {
    private List<StatusInscritDTO> statusInscrits;
    private List<FiliereInscritDTO> filiereInscrit;
    private int inscrit;
    private int nonInscrit;
    private double payePurcent;
    private double notPayePurcent;
    private double dettePurcent;
}
