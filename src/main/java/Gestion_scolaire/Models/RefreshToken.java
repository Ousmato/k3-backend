package Gestion_scolaire.Models;

import Gestion_scolaire.Administrators.entity.AdministrationUsers;
import Gestion_scolaire.students.entity.Students;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String token = "";

    @OneToOne
    private AdministrationUsers administrationUsers;

    @OneToOne
    private Students students;


}
