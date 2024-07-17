package Gestion_scolaire.MailSender;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class PendingEmail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String toSend;
    private String fromAdmin;
    private String subject;
    private String body;
    private LocalDateTime createdTime;


}
