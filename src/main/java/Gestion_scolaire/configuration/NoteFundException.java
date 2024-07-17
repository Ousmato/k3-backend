package Gestion_scolaire.configuration;


public class NoteFundException extends  RuntimeException{
    // Constructeur qui accepte un message d'erreur
    public NoteFundException(String message) {
        super(message);
    }

    // Constructeur qui accepte un message d'erreur et une cause
    //public NoteFundException(String message, Throwable cause) {
        //super(message, cause);
    //}



}
