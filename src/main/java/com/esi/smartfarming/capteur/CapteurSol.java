package com.esi.smartfarming.capteur;

import com.esi.smartfarming.enums.NiveauReleve;
import com.esi.smartfarming.releve.ReleveNumerique;
import com.esi.smartfarming.zone.Zone;

import java.util.Date;

public class CapteurSol extends CapteurNumerique {
    private float ph;
    private float humidite;
    private float teneurAzote;

    public CapteurSol(String code, Zone zone, double seuilMin, double seuilMax, String unite,
                      float ph, float humidite, float teneurAzote) {
        super(code, zone, seuilMin, seuilMax, unite);
        this.ph = ph;
        this.humidite = humidite;
        this.teneurAzote = teneurAzote;
    }

    public float getPh() { return ph; }
    public void setPh(float ph) { this.ph = ph; }

    public float getHumidite() { return humidite; }
    public void setHumidite(float humidite) { this.humidite = humidite; }

    public float getTeneurAzote() { return teneurAzote; }
    public void setTeneurAzote(float teneurAzote) { this.teneurAzote = teneurAzote; }

    @Override
    public ReleveNumerique envoyerReleve() {
        NiveauReleve niveau = verifierSeuil(ph) ? NiveauReleve.NORMAL : NiveauReleve.AVERTISSEMENT;
        ReleveNumerique releve = new ReleveNumerique(historique.size() + 1, this, new Date(), niveau, ph, unite, this);
        historique.add(releve);
        return releve;
    }
}
