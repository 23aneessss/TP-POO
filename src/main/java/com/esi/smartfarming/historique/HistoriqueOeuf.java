package com.esi.smartfarming.historique;

import com.esi.smartfarming.zone.Zone;

import java.util.Date;

public class HistoriqueOeuf extends HistoriqueProduction {
    private static final long serialVersionUID = 1L;
    private int nombreOeufs;

    public HistoriqueOeuf(Zone zone, Date date, int nombreOeufs) {
        super(zone, date);
        this.nombreOeufs = nombreOeufs;
    }

    public int getNombreOeufs() { return nombreOeufs; }

    @Override
    public void enregistrer() {
        String nomZone = zone != null ? zone.getNom() : "N/A";
        System.out.println("[HistoriqueOeuf] Zone: " + nomZone + " | Date: " + date + " | Nombre d'oeufs: " + nombreOeufs);
    }
}
