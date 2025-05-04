package Gestion_scolaire.Emplois.dtos;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class DtoEmploiByWeeks {
    private LocalDate weekEnd;
    private LocalDate weekStart;
    private String status;
    private List<EmploiListForDtoEmploiWeek> emplois;

}

