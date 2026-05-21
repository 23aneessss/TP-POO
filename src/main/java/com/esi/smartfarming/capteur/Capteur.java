package com.esi.smartfarming.capteur;

import com.esi.smartfarming.core.Suspendable;
import com.esi.smartfarming.enums.StatutCapteur;
import com.esi.smartfarming.zone.Zone;

import java.io.Serializable;

public abstract class Capteur implements Suspendable, Serializable {
    private static final long serialVersionUID = 1L;

    protected String code;
    protected Zone zone;
    protected StatutCapteur statut;

    protected Capteur(String code, Zone zone) {
        this.code = code;
        this.zone = zone;
        this.statut = StatutCapteur.ACTIF;
    }

    public String getCode() { return code; }
    public Zone getZone() { return zone; }
    public StatutCapteur getStatut() { return statut; }

    @Override
    public void suspendre() { this.statut = StatutCapteur.SUSPENDU; }

    @Override
    public void activer() { this.statut = StatutCapteur.ACTIF; }

    public void marquerDefaillant() { this.statut = StatutCapteur.DEFAILLANT; }
}
