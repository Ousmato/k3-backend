package Gestion_scolaire.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class StudentDoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @ManyToOne
    private Inscription idInscription;

    @NotNull
    @ManyToOne
    private Documents idDocument;

    @NotNull
    @ManyToOne
    private Admin idAdmin;
}
