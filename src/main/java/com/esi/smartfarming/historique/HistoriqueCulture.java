package com.esi.smartfarming.historique;

import com.esi.smartfarming.zone.Zone;

import java.util.Date;

public class HistoriqueCulture extends HistoriqueProduction {
    private double rendement;

    public HistoriqueCulture(Zone zone, Date date, double rendement) {
        super(zone, date);
        this.rendement = rendement;
    }

    @Override
    public void enregistrer() {
    }
}
