package Gestion_scolaire.MailSender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EmailRetryTask {

    @Autowired
    private MessaSender messaSender;

    @Scheduled(fixedRate = 1800000) // retry every 30 minute seconds
    public void retryPendingEmails() {
        messaSender.retryPendingEmails();
    }
}
