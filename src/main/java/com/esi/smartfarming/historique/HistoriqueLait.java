package com.esi.smartfarming.historique;

import com.esi.smartfarming.zone.Zone;

import java.util.Date;

public class HistoriqueLait extends HistoriqueProduction {
    private static final long serialVersionUID = 1L;
    private double quantiteLait;

    public HistoriqueLait(Zone zone, Date date, double quantiteLait) {
        super(zone, date);
        this.quantiteLait = quantiteLait;
    }

    public double getQuantiteLait() { return quantiteLait; }

    @Override
    public void enregistrer() {
        String nomZone = zone != null ? zone.getNom() : "N/A";
        System.out.println("[HistoriqueLait] Zone: " + nomZone + " | Date: " + date + " | Quantite lait: " + quantiteLait + " L");
    }
}
