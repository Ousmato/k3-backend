package Gestion_scolaire.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Profile{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @ManyToOne
    private Teachers idTeacher;

    @NotNull
    @ManyToOne
    private Filiere idFiliere;

    @NotNull
    @ManyToOne
    private Admin idAdmin;
}
