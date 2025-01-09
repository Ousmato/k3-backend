package Gestion_scolaire.students.enumClass;

import Gestion_scolaire.configuration.NoteFundException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.Getter;

@Getter
@JsonDeserialize(using = InscriptionSeriesDeserializer.class)
public enum InscriptionSeries {

    //lycee
    TSECO("TSECO"),
    TSS("TSS"),
    TSEXP("TSEXP"),
    TSE("TSE"),
    TLL("TLL"),
    TAL("TAL"),
    Neant("Neant"),

    //lycce technique
    Gestion_Commerce("GCO"),
    Genie_Civil("GC"),
    Genie_Mecanique("GM"),
    Genie_ELectrique("GE"),
    Comptabilite_Finance("CF"),

    //diplome professionnelle
    Technique_Comptable("TC"),
    Secretaria_Direction("SD"),
    Dessin_Batiment("DB"),
    Batiment("BATIMENT"),
    ELectricite("ELECTRICITE"),
    ELECTRO_MECANIQUE("EM");


    private final String abbreviation;

    InscriptionSeries(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public static InscriptionSeries fromAbbreviation(String abbr) {
        System.out.println("------------------series----------------" + abbr);
        for (InscriptionSeries series : values()) {
            if (series.abbreviation.equalsIgnoreCase(abbr)) {
                return series;
            }
        }
        throw new NoteFundException("Aucune série correspondante trouvée pour abb : " + abbr);
    }

}
