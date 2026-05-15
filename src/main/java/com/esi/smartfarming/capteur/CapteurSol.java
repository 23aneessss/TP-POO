package com.esi.smartfarming.capteur;

import com.esi.smartfarming.enums.NiveauReleve;
import com.esi.smartfarming.releve.ReleveNumerique;
import com.esi.smartfarming.zone.Zone;

import java.util.Date;

public class CapteurSol extends CapteurNumerique {
    private String typeCapture;

    public CapteurSol(String code, Zone zone, double seuilMin, double seuilMax, String unite, String typeCapture) {
        super(code, zone, seuilMin, seuilMax, unite);
        this.typeCapture = typeCapture;
    }

    public String getTypeCapture() { return typeCapture; }

    @Override
    public ReleveNumerique envoyerReleve() {
        double valeur = (seuilMin + seuilMax) / 2.0;
        NiveauReleve niveau = verifierSeuil(valeur) ? NiveauReleve.NORMAL : NiveauReleve.AVERTISSEMENT;
        ReleveNumerique releve = new ReleveNumerique(historique.size() + 1, this, new Date(), niveau, valeur, unite, this);
        historique.add(releve);
        return releve;
    }
}
