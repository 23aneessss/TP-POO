package com.esi.smartfarming.historique;

import com.esi.smartfarming.zone.Zone;

import java.util.Date;

public class HistoriqueAquacole extends HistoriqueProduction {
    private static final long serialVersionUID = 1L;
    private double poidsRecolte;

    public HistoriqueAquacole(Zone zone, Date date, double poidsRecolte) {
        super(zone, date);
        this.poidsRecolte = poidsRecolte;
    }

    public double getPoidsRecolte() { return poidsRecolte; }

    @Override
    public void enregistrer() {
        String nomZone = zone != null ? zone.getNom() : "N/A";
        System.out.println("[HistoriqueAquacole] Zone: " + nomZone + " | Date: " + date + " | Poids recolte: " + poidsRecolte + " kg");
    }
}
