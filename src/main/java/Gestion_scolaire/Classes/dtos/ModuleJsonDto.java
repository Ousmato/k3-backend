package Gestion_scolaire.Classes.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ModuleJsonDto {

    @JsonProperty("ECUE")
    private String ecue;

    @JsonProperty("Credits")
    private int credits;

    @JsonProperty("Objectif")
    private String objectif;

    @JsonProperty("CM")
    private int cm;

    @JsonProperty("TD")
    private int td;

    @JsonProperty("TPE")
    private int tpe;

    @JsonProperty("VHT")
    private int vht;

    @JsonProperty("TP")
    private int tp;

}
