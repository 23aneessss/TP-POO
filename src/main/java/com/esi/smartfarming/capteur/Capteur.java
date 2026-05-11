package com.esi.smartfarming.capteur;

import com.esi.smartfarming.core.Suspendable;
import com.esi.smartfarming.enums.StatutCapteur;
import com.esi.smartfarming.zone.Zone;


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

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public Zone getZone() { return zone; }
    public void setZone(Zone zone) { this.zone = zone; }

    public StatutCapteur getStatut() { return statut; }
    public void setStatut(StatutCapteur statut) { this.statut = statut; }

    public double getSeuilMin() { return seuilMin; }
    public void setSeuilMin(double seuilMin) { this.seuilMin = seuilMin; }

    public double getSeuilMax() { return seuilMax; }
    public void setSeuilMax(double seuilMax) { this.seuilMax = seuilMax; }
}
