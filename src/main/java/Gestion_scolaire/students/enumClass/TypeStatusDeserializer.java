package Gestion_scolaire.students.enumClass;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class TypeStatusDeserializer extends JsonDeserializer<Type_status> {

    @Override
    public Type_status deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getText().replaceAll(" ", "_"); // Remplace les espaces par des underscores
        try {
            return Type_status.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new IOException("Valeur non valide pour Type_status : " + value, e);
        }
    }
}
