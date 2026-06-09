package com.esi.smartfarming.animal;

import com.esi.smartfarming.historique.HistoriqueLait;
import com.esi.smartfarming.zone.ZoneElevage;

import java.util.Date;

/**
 * Animal ruminant (vache, brebis, chèvre…) dont la production est mesurée
 * en litres de lait par session.
 * <p>
 * Limite de capacité de la zone : 50 animaux.
 * La production est enregistrée dans un {@link com.esi.smartfarming.historique.HistoriqueLait}.
 */
public class Ruminant extends Animal {
    private static final long serialVersionUID = 1L;
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
