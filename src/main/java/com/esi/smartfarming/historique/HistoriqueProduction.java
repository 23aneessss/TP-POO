package com.esi.smartfarming.historique;

import com.esi.smartfarming.zone.Zone;

import java.util.Date;

@SuppressWarnings("unused")
public abstract class HistoriqueProduction {
    protected Zone zone;
    protected Date date;

    protected HistoriqueProduction(Zone zone, Date date) {
        this.zone = zone;
        this.date = date;
    }
}
