package com.esi.smartfarming.capteur;

import com.esi.smartfarming.enums.NiveauReleve;
import com.esi.smartfarming.releve.ReleveNumerique;
import com.esi.smartfarming.zone.Zone;

import java.util.Date;

public class CapteurEau extends CapteurNumerique {
    private static final long serialVersionUID = 1L;
    private double temperateur;
    private double oxygene;
    private double ph;
    private String typeCapture;

    private double tempMin, tempMax;
    private double oxyMin,  oxyMax;
    private double phMin,   phMax;

    public CapteurEau(String code, Zone zone, String unite,
                      double temperateur, double oxygene, double ph, String typeCapture,
                      double tempMin, double tempMax,
                      double oxyMin,  double oxyMax,
                      double phMin,   double phMax) {
        super(code, zone, unite);
        this.temperateur = temperateur;
        this.oxygene     = oxygene;
        this.ph          = ph;
        this.typeCapture = typeCapture;
        this.tempMin = tempMin; this.tempMax = tempMax;
        this.oxyMin  = oxyMin;  this.oxyMax  = oxyMax;
        this.phMin   = phMin;   this.phMax   = phMax;
    }

    public double getTemperateur() { return temperateur; }
    public void setTemperateur(double temperateur) { this.temperateur = temperateur; }

    public double getOxygene() { return oxygene; }
    public void setOxygene(double oxygene) { this.oxygene = oxygene; }

    public double getPh() { return ph; }
    public void setPh(double ph) { this.ph = ph; }

    public String getTypeCapture() { return typeCapture; }
    public void setTypeCapture(String typeCapture) { this.typeCapture = typeCapture; }

    public double getTempMin() { return tempMin; } public double getTempMax() { return tempMax; }
    public double getOxyMin()  { return oxyMin;  } public double getOxyMax()  { return oxyMax;  }
    public double getPhMin()   { return phMin;   } public double getPhMax()   { return phMax;   }

    public boolean verifierTemperature() { return temperateur >= tempMin && temperateur <= tempMax; }
    public boolean verifierOxygene()     { return oxygene     >= oxyMin  && oxygene     <= oxyMax;  }
    public boolean verifierPh()          { return ph          >= phMin   && ph          <= phMax;   }

    @Override
    public ReleveNumerique envoyerReleve() {
        temperateur = genererValeur(tempMin, tempMax);
        oxygene     = genererValeur(oxyMin, oxyMax);
        ph          = genererValeur(phMin, phMax);
        boolean ok = verifierTemperature() && verifierOxygene() && verifierPh();
        NiveauReleve niveau = ok ? NiveauReleve.NORMAL : NiveauReleve.AVERTISSEMENT;
        ReleveNumerique releve = new ReleveNumerique(historique.size() + 1, this, new Date(), niveau, temperateur, unite, this);
        historique.add(releve);
        return releve;
    }
}
