package com.esi.smartfarming.historique;

import com.esi.smartfarming.zone.Zone;

import java.util.Date;

@SuppressWarnings("unused")
public class HistoriqueLait extends HistoriqueProduction {
    private double quantiteLait;

    public HistoriqueLait(Zone zone, Date date, double quantiteLait) {
        super(zone, date);
        this.quantiteLait = quantiteLait;
    }
}
