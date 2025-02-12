package Gestion_scolaire.students.enumClass;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = TypeStatusDeserializer.class)
public enum Type_status {
    REGULIER,
    CANDIDAT_LIBRE,
    PROFESSIONNEL_PRIVEE,
    PROFESSIONNEL_ETAT,
    PROFESSIONNEL_COLLECTIVITE,

    FORMATION_CONTINUE,

}
