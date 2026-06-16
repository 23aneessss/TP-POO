package com.esi.smartfarming.animal;

import com.esi.smartfarming.historique.HistoriqueOeuf;
import com.esi.smartfarming.zone.ZoneElevage;

import java.util.Date;

public class Volaille extends Animal {
    private static final long serialVersionUID = 1L;
    public Volaille(int numero, String espece, int age, double poids) {
        super(numero, espece, age, poids);
    }

    public void enregistrerProduction(int nbOeufs, Date date) {
        HistoriqueOeuf h = new HistoriqueOeuf(null, date, nbOeufs);
        h.enregistrer();
    }

    @Override
    public boolean verifierLimites(ZoneElevage z) {
        return z.getAnimaux().size() <= 200;
    }
}
