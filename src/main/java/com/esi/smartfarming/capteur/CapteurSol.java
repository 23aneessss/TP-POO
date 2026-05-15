package com.esi.smartfarming.capteur;

import com.esi.smartfarming.enums.NiveauReleve;
import com.esi.smartfarming.releve.ReleveNumerique;
import com.esi.smartfarming.zone.Zone;

import java.util.Date;

public class CapteurSol extends CapteurNumerique {
    private double ph;
    private double humidite;
    private double teneurAzote;

    public CapteurSol(String code, Zone zone, double seuilMin, double seuilMax, String unite,
                      double ph, double humidite, double teneurAzote) {
        super(code, zone, seuilMin, seuilMax, unite);
        this.ph = ph;
        this.humidite = humidite;
        this.teneurAzote = teneurAzote;
    }

    public double getPh() { return ph; }
    public void setPh(double ph) { this.ph = ph; }

    public double getHumidite() { return humidite; }
    public void setHumidite(double humidite) { this.humidite = humidite; }

    public double getTeneurAzote() { return teneurAzote; }
    public void setTeneurAzote(double teneurAzote) { this.teneurAzote = teneurAzote; }

    @Override
    public ReleveNumerique envoyerReleve() {
        NiveauReleve niveau = verifierSeuil(ph) ? NiveauReleve.NORMAL : NiveauReleve.AVERTISSEMENT;
        ReleveNumerique releve = new ReleveNumerique(historique.size() + 1, this, new Date(), niveau, ph, unite, this);
        historique.add(releve);
        return releve;
    }
}
