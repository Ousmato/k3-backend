package Gestion_scolaire.Teachers.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class Teacher_specialite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "La spécialité est obligatoire")
    @ManyToOne
    private  Specialites idSpecialite;

    @NotNull(message = "L'enseignant est obligatoire")
    @ManyToOne
    private  Teachers idTeacher;

}
