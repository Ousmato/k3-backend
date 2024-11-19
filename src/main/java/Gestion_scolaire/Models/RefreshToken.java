package Gestion_scolaire.Models;

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
    private Admin admin;


}
