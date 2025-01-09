package Gestion_scolaire.students.enumClass;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = TypeStatusDeserializer.class)
public enum Type_status {
    REGULIER,
    PROFESSIONNEL_ETAT,
    PROFESSIONNEL_COLLECTIVITE,
    PROFESSIONNEL_PRIVEE,
    FORMATION_CONTINUE,
    CANDIDAT_LIBRE
}
