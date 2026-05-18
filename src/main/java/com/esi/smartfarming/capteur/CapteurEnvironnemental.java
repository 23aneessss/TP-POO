package com.esi.smartfarming.capteur;

import com.esi.smartfarming.enums.NiveauReleve;
import com.esi.smartfarming.releve.ReleveNumerique;
import com.esi.smartfarming.zone.Zone;

import java.util.Date;

public class CapteurEnvironnemental extends CapteurNumerique {
    private double temperature;
    private double humidite;
    private double pluviometrie;

    private double tempMin, tempMax;
    private double humMin,  humMax;
    private double pluvMin, pluvMax;

    public CapteurEnvironnemental(String code, Zone zone, String unite,
                                   double temperature, double humidite, double pluviometrie,
                                   double tempMin, double tempMax,
                                   double humMin,  double humMax,
                                   double pluvMin, double pluvMax) {
        super(code, zone, unite);
        this.temperature   = temperature;
        this.humidite      = humidite;
        this.pluviometrie  = pluviometrie;
        this.tempMin = tempMin; this.tempMax = tempMax;
        this.humMin  = humMin;  this.humMax  = humMax;
        this.pluvMin = pluvMin; this.pluvMax = pluvMax;
    }

    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }

    public double getHumidite() { return humidite; }
    public void setHumidite(double humidite) { this.humidite = humidite; }

    public double getPluviometrie() { return pluviometrie; }
    public void setPluviometrie(double pluviometrie) { this.pluviometrie = pluviometrie; }

    public double getTempMin() { return tempMin; } public double getTempMax() { return tempMax; }
    public double getHumMin()  { return humMin;  } public double getHumMax()  { return humMax;  }
    public double getPluvMin() { return pluvMin; } public double getPluvMax() { return pluvMax; }

    public boolean verifierTemperature()  { return temperature  >= tempMin && temperature  <= tempMax; }
    public boolean verifierHumidite()     { return humidite     >= humMin  && humidite     <= humMax;  }
    public boolean verifierPluviometrie() { return pluviometrie >= pluvMin && pluviometrie <= pluvMax; }

    @Override
    public ReleveNumerique envoyerReleve() {
        boolean ok = verifierTemperature() && verifierHumidite() && verifierPluviometrie();
        NiveauReleve niveau = ok ? NiveauReleve.NORMAL : NiveauReleve.AVERTISSEMENT;
        ReleveNumerique releve = new ReleveNumerique(historique.size() + 1, this, new Date(), niveau, temperature, unite, this);
        historique.add(releve);
        return releve;
    }
}
