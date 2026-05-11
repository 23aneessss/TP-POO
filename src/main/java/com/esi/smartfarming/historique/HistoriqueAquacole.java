package com.esi.smartfarming.historique;

import com.esi.smartfarming.zone.Zone;

import java.util.Date;

@SuppressWarnings("unused")
public class HistoriqueAquacole extends HistoriqueProduction {
    private double poidsRecolte;

    public HistoriqueAquacole(Zone zone, Date date, double poidsRecolte) {
        super(zone, date);
        this.poidsRecolte = poidsRecolte;
    }
}
