package Gestion_scolaire.students.enumClass;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class InscriptionSeriesDeserializer extends JsonDeserializer<InscriptionSeries> {

    @Override
    public InscriptionSeries deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String abbr = p.getText().toUpperCase();
        return InscriptionSeries.fromAbbreviation(abbr);
    }
}
