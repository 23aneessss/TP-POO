package com.esi.smartfarming.animal;

import java.util.Date;

public class Volaille extends Animal {
    public Volaille(int numero, String espece, int age, double poids) {
        super(numero, espece, age, poids);
    }

    public void enregistrerProduction(int nbOeufs, Date date) {
    }

    @Override
    protected boolean verifierLimites(com.esi.smartfarming.zone.ZoneElevage z) {
        return true;
    }
}
