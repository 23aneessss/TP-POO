package com.esi.smartfarming.historique;

import com.esi.smartfarming.zone.Zone;

import java.util.Date;

public class HistoriqueAquacole extends HistoriqueProduction {
    private double poidsRecolte;

    public HistoriqueAquacole(Zone zone, Date date, double poidsRecolte) {
        super(zone, date);
        this.poidsRecolte = poidsRecolte;
    }

    @Override
    public void enregistrer() {
    }
}
