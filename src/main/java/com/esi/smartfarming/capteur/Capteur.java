package com.esi.smartfarming.capteur;

import com.esi.smartfarming.core.Suspendable;
import com.esi.smartfarming.enums.StatutCapteur;
import com.esi.smartfarming.zone.Zone;

@SuppressWarnings("unused")
public abstract class Capteur implements Suspendable {
    protected String code;
    protected Zone zone;
    protected StatutCapteur statut;
    protected double seuilMin;
    protected double seuilMax;

    protected Capteur(String code, Zone zone, double seuilMin, double seuilMax) {
        this.code = code;
        this.zone = zone;
        this.seuilMin = seuilMin;
        this.seuilMax = seuilMax;
        this.statut = StatutCapteur.ACTIF;
    }

    @Override
    public void suspendre() {}

    @Override
    public void activer() {}
}
