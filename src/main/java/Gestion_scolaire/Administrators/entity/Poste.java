package Gestion_scolaire.Administrators.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Poste {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "L'admin est obligatoire")
    @ManyToOne
    private Admin currentAdmin;

    @NotNull(message = "L'admin est obligatoire")
    @ManyToOne
    private Admin defaultAdmin;

    @NotNull(message = "La date est obligatoire")
    private LocalDateTime dateTime = LocalDateTime.now();

    @NotNull(message = "La date est obligatoire")
    private LocalDateTime updateDateTime = LocalDateTime.now();

    private boolean active = true;

    @NotBlank(message = "OTP est obligatoire")
    private String otp = "otp";

}
