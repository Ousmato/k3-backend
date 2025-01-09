package Gestion_scolaire.Teachers.enumClasse;


import com.fasterxml.jackson.annotation.JsonCreator;

public enum Teachers_status {
    Vacataire,
    Permanent;

    @JsonCreator
    public static Teachers_status fromValue(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return Teachers_status.valueOf(value);
    }
}
