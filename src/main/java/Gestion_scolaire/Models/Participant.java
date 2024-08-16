package Gestion_scolaire.Models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Participant{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Studens idStudent;

    @ManyToOne
    StudentGroupe idStudentGroup;
    
}
