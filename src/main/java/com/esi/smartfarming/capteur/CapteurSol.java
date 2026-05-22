package com.esi.smartfarming.capteur;

import com.esi.smartfarming.enums.NiveauReleve;
import com.esi.smartfarming.releve.ReleveNumerique;
import com.esi.smartfarming.zone.Zone;

import java.util.Date;

public class CapteurSol extends CapteurNumerique {
    private static final long serialVersionUID = 1L;
    private double ph;
    private double humidite;
    private double teneurAzote;

    private double phMin,    phMax;
    private double humMin,   humMax;
    private double azoteMin, azoteMax;

    public CapteurSol(String code, Zone zone, String unite,
                      double ph, double humidite, double teneurAzote,
                      double phMin,    double phMax,
                      double humMin,   double humMax,
                      double azoteMin, double azoteMax) {
        super(code, zone, unite);
        this.ph          = ph;
        this.humidite    = humidite;
        this.teneurAzote = teneurAzote;
        this.phMin    = phMin;    this.phMax    = phMax;
        this.humMin   = humMin;   this.humMax   = humMax;
        this.azoteMin = azoteMin; this.azoteMax = azoteMax;
    }

    public double getPh() { return ph; }
    public void setPh(double ph) { this.ph = ph; }

    public double getHumidite() { return humidite; }
    public void setHumidite(double humidite) { this.humidite = humidite; }

    public double getTeneurAzote() { return teneurAzote; }
    public void setTeneurAzote(double teneurAzote) { this.teneurAzote = teneurAzote; }

    public double getPhMin()    { return phMin;    } public double getPhMax()    { return phMax;    }
    public double getHumMin()   { return humMin;   } public double getHumMax()   { return humMax;   }
    public double getAzoteMin() { return azoteMin; } public double getAzoteMax() { return azoteMax; }

    public boolean verifierPh()         { return ph          >= phMin    && ph          <= phMax;    }
    public boolean verifierHumidite()   { return humidite    >= humMin   && humidite    <= humMax;   }
    public boolean verifierTeneurAzote(){ return teneurAzote >= azoteMin && teneurAzote <= azoteMax; }

    @Override
    public ReleveNumerique envoyerReleve() {
        boolean ok = verifierPh() && verifierHumidite() && verifierTeneurAzote();
        NiveauReleve niveau = ok ? NiveauReleve.NORMAL : NiveauReleve.AVERTISSEMENT;
        ReleveNumerique releve = new ReleveNumerique(historique.size() + 1, this, new Date(), niveau, ph, unite, this);
        historique.add(releve);
        return releve;
    }
}
