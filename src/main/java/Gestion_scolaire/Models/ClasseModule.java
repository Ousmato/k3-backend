package Gestion_scolaire.Models;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
public class ClasseModule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  long id;

    @ManyToOne
    private StudentsClasse idStudentClasse;

    @ManyToOne
    private UE idUE;

}
