package com.esi.smartfarming.historique;

import com.esi.smartfarming.zone.Zone;

import java.io.Serializable;
import java.util.Date;

public abstract class HistoriqueProduction implements Serializable {
    private static final long serialVersionUID = 1L;
    protected Zone zone;
    protected Date date;

    protected HistoriqueProduction(Zone zone, Date date) {
        this.zone = zone;
        this.date = date;
    }

    public Zone getZone() { return zone; }
    public Date getDate() { return date; }

    public abstract void enregistrer();
}
