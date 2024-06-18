package Gestion_scolaire;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class GestionScolaireApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionScolaireApplication.class, args);
	}

}
