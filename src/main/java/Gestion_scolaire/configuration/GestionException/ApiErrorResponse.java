package Gestion_scolaire.configuration.GestionException;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiErrorResponse {
    private int status;
    private String message;
    private String timestamp;
    private String path;


}
