package com.esi.smartfarming.historique;

import com.esi.smartfarming.zone.Zone;

import java.util.Date;

public class HistoriqueOeuf extends HistoriqueProduction {
    private int nombreOeufs;

    public HistoriqueOeuf(Zone zone, Date date, int nombreOeufs) {
        super(zone, date);
        this.nombreOeufs = nombreOeufs;
    }

    public int getNombreOeufs() { return nombreOeufs; }
    public void setNombreOeufs(int nombreOeufs) { this.nombreOeufs = nombreOeufs; }
}
