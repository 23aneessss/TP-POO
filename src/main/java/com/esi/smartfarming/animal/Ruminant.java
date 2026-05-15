package com.esi.smartfarming.animal;

import com.esi.smartfarming.historique.HistoriqueLait;
import com.esi.smartfarming.zone.ZoneElevage;

import java.util.Date;

public class Ruminant extends Animal {
    public Ruminant(int numero, String espece, int age, double poids) {
        super(numero, espece, age, poids);
    }

    public void enregistrerProduction(double lait, Date date) {
        HistoriqueLait h = new HistoriqueLait(null, date, lait);
        h.enregistrer();
    }

    @Override
    public boolean verifierLimites(ZoneElevage z) {
        return z.getAnimaux().size() <= 50;
    }
}
