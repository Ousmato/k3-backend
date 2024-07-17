package Gestion_scolaire.Repositories;

import Gestion_scolaire.MailSender.PendingEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PendingEmailRepository extends JpaRepository<PendingEmail, Long> {
}
