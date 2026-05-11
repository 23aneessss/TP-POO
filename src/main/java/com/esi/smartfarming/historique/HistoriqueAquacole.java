package com.esi.smartfarming.historique;

import com.esi.smartfarming.zone.Zone;

import java.util.Date;

public class HistoriqueAquacole extends HistoriqueProduction {
    private double poidsRecolte;

    public HistoriqueAquacole(Zone zone, Date date, double poidsRecolte) {
        super(zone, date);
        this.poidsRecolte = poidsRecolte;
    }

    public double getPoidsRecolte() { return poidsRecolte; }
    public void setPoidsRecolte(double poidsRecolte) { this.poidsRecolte = poidsRecolte; }
}
