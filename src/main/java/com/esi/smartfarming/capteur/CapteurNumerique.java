package com.esi.smartfarming.capteur;

import com.esi.smartfarming.releve.ReleveNumerique;
import com.esi.smartfarming.zone.Zone;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public abstract class CapteurNumerique extends Capteur {
    protected String unite;
    protected List<ReleveNumerique> historique;

    protected CapteurNumerique(String code, Zone zone, double seuilMin, double seuilMax, String unite) {
        super(code, zone, seuilMin, seuilMax);
        this.unite = unite;
        this.historique = new ArrayList<>();
    }
}
