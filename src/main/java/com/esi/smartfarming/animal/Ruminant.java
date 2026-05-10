package com.esi.smartfarming.animal;

import java.util.Date;

public class Ruminant extends Animal {
    public Ruminant(int numero, String espece, int age, double poids) {
        super(numero, espece, age, poids);
    }

    public void enregistrerProduction(double lait, Date date) {
    }

    @Override
    protected boolean verifierLimites(com.esi.smartfarming.zone.ZoneElevage z) {
        return true;
    }
}
