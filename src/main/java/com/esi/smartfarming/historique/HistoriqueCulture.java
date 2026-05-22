package com.esi.smartfarming.historique;

import com.esi.smartfarming.zone.Zone;

import java.util.Date;

public class HistoriqueCulture extends HistoriqueProduction {
    private static final long serialVersionUID = 1L;
    private double rendement;

    public HistoriqueCulture(Zone zone, Date date, double rendement) {
        super(zone, date);
        this.rendement = rendement;
    }

    public double getRendement() { return rendement; }

    @Override
    public void enregistrer() {
        String nomZone = zone != null ? zone.getNom() : "N/A";
        System.out.println("[HistoriqueCulture] Zone: " + nomZone + " | Date: " + date + " | Rendement: " + rendement + " t/ha");
    }
}
