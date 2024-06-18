package Gestion_scolaire.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class StudentsPresence {

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private LocalDate date;

    @NotNull
    private String jours;

    @NotNull
    private boolean status = true;

    @ManyToOne
    private Studens idStudents;

    @ManyToOne
    private Seances idSeance;


}
