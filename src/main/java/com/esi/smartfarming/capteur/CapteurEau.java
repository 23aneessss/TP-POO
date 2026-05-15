package com.esi.smartfarming.capteur;

import com.esi.smartfarming.enums.NiveauReleve;
import com.esi.smartfarming.releve.ReleveNumerique;
import com.esi.smartfarming.zone.Zone;

import java.util.Date;

public class CapteurEau extends CapteurNumerique {
    private double temperateur;
    private double oxygene;
    private double ph;
    private String typeCapture;

    public CapteurEau(String code, Zone zone, double seuilMin, double seuilMax, String unite,
                      double temperateur, double oxygene, double ph, String typeCapture) {
        super(code, zone, seuilMin, seuilMax, unite);
        this.temperateur = temperateur;
        this.oxygene = oxygene;
        this.ph = ph;
        this.typeCapture = typeCapture;
    }

    public double getTemperateur() { return temperateur; }
    public void setTemperateur(double temperateur) { this.temperateur = temperateur; }

    public double getOxygene() { return oxygene; }
    public void setOxygene(double oxygene) { this.oxygene = oxygene; }

    public double getPh() { return ph; }
    public void setPh(double ph) { this.ph = ph; }

    public String getTypeCapture() { return typeCapture; }
    public void setTypeCapture(String typeCapture) { this.typeCapture = typeCapture; }

    @Override
    public ReleveNumerique envoyerReleve() {
        NiveauReleve niveau = verifierSeuil(temperateur) ? NiveauReleve.NORMAL : NiveauReleve.AVERTISSEMENT;
        ReleveNumerique releve = new ReleveNumerique(historique.size() + 1, this, new Date(), niveau, temperateur, unite, this);
        historique.add(releve);
        return releve;
    }
}
