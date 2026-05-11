package com.esi.smartfarming.historique;

import com.esi.smartfarming.zone.Zone;

import java.util.Date;

public class HistoriqueCulture extends HistoriqueProduction {
    private double rendement;

    public HistoriqueCulture(Zone zone, Date date, double rendement) {
        super(zone, date);
        this.rendement = rendement;
    }

    public double getRendement() { return rendement; }
    public void setRendement(double rendement) { this.rendement = rendement; }
}
