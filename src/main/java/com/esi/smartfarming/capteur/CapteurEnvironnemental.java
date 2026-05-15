package com.esi.smartfarming.capteur;

import com.esi.smartfarming.enums.NiveauReleve;
import com.esi.smartfarming.releve.ReleveNumerique;
import com.esi.smartfarming.zone.Zone;

import java.util.Date;

public class CapteurEnvironnemental extends CapteurNumerique {
    private double temperature;
    private double humidite;
    private double pluviometrie;

    public CapteurEnvironnemental(String code, Zone zone, double seuilMin, double seuilMax, String unite,
                                   double temperature, double humidite, double pluviometrie) {
        super(code, zone, seuilMin, seuilMax, unite);
        this.temperature = temperature;
        this.humidite = humidite;
        this.pluviometrie = pluviometrie;
    }

    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }

    public double getHumidite() { return humidite; }
    public void setHumidite(double humidite) { this.humidite = humidite; }

    public double getPluviometrie() { return pluviometrie; }
    public void setPluviometrie(double pluviometrie) { this.pluviometrie = pluviometrie; }

    @Override
    public ReleveNumerique envoyerReleve() {
        NiveauReleve niveau = verifierSeuil(temperature) ? NiveauReleve.NORMAL : NiveauReleve.AVERTISSEMENT;
        ReleveNumerique releve = new ReleveNumerique(historique.size() + 1, this, new Date(), niveau, temperature, unite, this);
        historique.add(releve);
        return releve;
    }
}
