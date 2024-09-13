package Gestion_scolaire.configuration.GestionException;

import Gestion_scolaire.configuration.NoteFundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NoteFundException.class)
    public ApiErrorResponse handleNoteFundException(NoteFundException ex, WebRequest request) {
        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                request.getDescription(false)
        );
        return response;
    }


    // Gestion de l'exception pour ressource non trouvée (exemple)
//    @ExceptionHandler(ResourceNotFoundException.class)
//    public ResponseEntity<ApiErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
//        ApiErrorResponse response = new ApiErrorResponse(
//                HttpStatus.NOT_FOUND.value(), // Statut 404
//                ex.getMessage(),
//                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
//                request.getDescription(false)
//        );
//        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
//    }
//
//    // Gestion de l'exception pour accès interdit (exemple)
//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<ApiErrorResponse> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
//        ApiErrorResponse response = new ApiErrorResponse(
//                HttpStatus.FORBIDDEN.value(), // Statut 403
//                "Accès refusé : " + ex.getMessage(),
//                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
//                request.getDescription(false)
//        );
//        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
//    }

//    -----------------------------------------------------------------------------
    @ExceptionHandler(Exception.class)
    public ApiErrorResponse handleGenericException(Exception ex, WebRequest request) {
        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                request.getDescription(false)
        );
        return response ;
    }
}
