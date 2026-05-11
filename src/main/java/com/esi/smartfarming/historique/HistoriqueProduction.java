package com.esi.smartfarming.historique;

import com.esi.smartfarming.zone.Zone;

import java.util.Date;

public abstract class HistoriqueProduction {
    protected Zone zone;
    protected Date date;

    protected HistoriqueProduction(Zone zone, Date date) {
        this.zone = zone;
        this.date = date;
    }

    public Zone getZone() { return zone; }
    public void setZone(Zone zone) { this.zone = zone; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
}
