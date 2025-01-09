package Gestion_scolaire.Dto_classe;

import lombok.Data;

@Data
public class DTO_response_string {
    private int status;
    private String message;

    public static DTO_response_string fromMessage(String msg){
        DTO_response_string res = new DTO_response_string();
        res.setMessage(msg);
//        res.setStatus(status);
        return res;
    }

    public  static  DTO_response_string addMessage(){
        DTO_response_string res = new DTO_response_string();
        res.setMessage("Ajout effectuée avec succés ");
        return res;
    }

    public static DTO_response_string updateMessage(){
        DTO_response_string res = new DTO_response_string();
        res.setMessage("Mise à jour effectuée avec succés");
        return res;
    }
}
