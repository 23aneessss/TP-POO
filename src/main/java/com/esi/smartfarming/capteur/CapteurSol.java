package com.esi.smartfarming.capteur;

import com.esi.smartfarming.releve.ReleveNumerique;
import com.esi.smartfarming.zone.Zone;

public class CapteurSol extends CapteurNumerique {
    private String typeCapture;

    public CapteurSol(String code, Zone zone, double seuilMin, double seuilMax, String unite, String typeCapture) {
        super(code, zone, seuilMin, seuilMax, unite);
        this.typeCapture = typeCapture;
    }

    @Override
    public ReleveNumerique envoyerReleve() { return creerEtEnregistrerReleve(seuilMin); }
}
