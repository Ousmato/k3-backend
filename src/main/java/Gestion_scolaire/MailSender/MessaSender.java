package Gestion_scolaire.MailSender;

import Gestion_scolaire.Models.Admin;
import Gestion_scolaire.Models.Studens;
import Gestion_scolaire.Models.Teachers;
import Gestion_scolaire.Repositories.PendingEmailRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessaSender {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private PendingEmailRepository pendingEmailRepository;

        @Transactional
        public void sendSimpleMail( PendingEmail pendingEmail){
                   // Creating a simple mail message
                SimpleMailMessage mailMessage = new SimpleMailMessage();

            try {
                mailMessage.setFrom(pendingEmail.getFromAdmin());
                mailMessage.setTo(pendingEmail.getToSend());
                mailMessage.setText(pendingEmail.getBody());
                mailMessage.setSubject(pendingEmail.getSubject());
                javaMailSender.send(mailMessage);
//                return DTO_response_string.fromMessage("Mail Sent Successfully...", 200);

            }catch (MailException e){
                PendingEmail pe = new PendingEmail();
                pe.setToSend(pendingEmail.getToSend());
                pe.setFromAdmin(pendingEmail.getFromAdmin());
                pe.setSubject(pendingEmail.getSubject());
                pe.setBody(pendingEmail.getBody());
                pe.setCreatedTime(LocalDateTime.now());
                pendingEmailRepository.save(pendingEmail);
            }

        }

        //::::::::::::::message qui doit etre envoyer

    @Transactional
    public void retryPendingEmails() {
        List<PendingEmail> pendingEmails = pendingEmailRepository.findAll();
        for (PendingEmail pendingEmail : pendingEmails) {
            try {
                SimpleMailMessage mailMessage = new SimpleMailMessage();

                mailMessage.setTo(pendingEmail.getToSend());
                mailMessage.setFrom(pendingEmail.getFromAdmin());
                mailMessage.setSubject(pendingEmail.getSubject());
                mailMessage.setText(pendingEmail.getBody());
                javaMailSender.send(mailMessage);
//                ----------------------------delete message after try and sent
                pendingEmailRepository.delete(pendingEmail);
            } catch (MailException e) {
                // log and continue
            }
        }
    }
    public String message(Studens studens, String password){
        return  ", Vous êtes invité à vous connecter à votre compte avec votre adresse email : %s et votre mot de passe : %s".formatted(studens.getEmail(), password);

    }
//    -------------------------message teacher
    public String messageTeacher(Teachers teacher, String password){
        return  ", Vous êtes invité à vous connecter à votre compte avec votre adresse email : %s et votre mot de passe : %s".formatted(teacher.getEmail(), password);

    }

    public String messageAdmin(Admin admin, String password){
        return  ", Vous êtes invité à vous connecter à votre compte avec votre adresse email : %s et votre mot de passe : %s".formatted(admin.getEmail(), password);

    }

    public String messageResetPassword(String prenom, String link, String token) {
        return "Bonjour M. %s,\n\nCliquez sur le lien ci-dessous pour réinitialiser votre mot de passe : \n%s\n\nCode de confirmation : %s\n\nSi vous n'avez pas demandé cette réinitialisation, veuillez ignorer cet email.".formatted(prenom, link, token);
    }

}
