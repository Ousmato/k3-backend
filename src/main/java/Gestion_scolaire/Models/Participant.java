package Gestion_scolaire.Models;

import jakarta.persistence.*;
import jakarta.validation.Validation;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class Participant{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @ManyToOne
    private Inscription idInscription;

    @ManyToOne
    private StudentGroupe idStudentGroup;

    @NotNull(message = "Admin est obligatoire")
    @ManyToOne
    private Admin idAdmin;


    
}
