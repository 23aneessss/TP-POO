package com.esi.smartfarming.alimentation;

@SuppressWarnings("unused")
public class ProgrammeAlimentation {
    private String typeAliment;
    private double quantiteParRepas;

    public ProgrammeAlimentation(String typeAliment, double quantiteParRepas) {
        this.typeAliment = typeAliment;
        this.quantiteParRepas = quantiteParRepas;
    }
}
