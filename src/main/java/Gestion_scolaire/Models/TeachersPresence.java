package Gestion_scolaire.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class TeachersPresence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private boolean observation = true;

    @ManyToOne
    private Seances idSeance;


}
