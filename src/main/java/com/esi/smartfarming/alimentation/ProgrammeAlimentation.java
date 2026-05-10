package com.esi.smartfarming.alimentation;

public class ProgrammeAlimentation {
    private String typeAliment;
    private double quantiteParRepas;

    public ProgrammeAlimentation(String typeAliment, double quantiteParRepas) {
        this.typeAliment = typeAliment;
        this.quantiteParRepas = quantiteParRepas;
    }

    public String getTypeAliment() { return typeAliment; }
    public double getQuantiteParRepas() { return quantiteParRepas; }
}
