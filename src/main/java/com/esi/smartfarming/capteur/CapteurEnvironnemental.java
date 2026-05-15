package com.esi.smartfarming.capteur;

import com.esi.smartfarming.enums.NiveauReleve;
import com.esi.smartfarming.releve.ReleveNumerique;
import com.esi.smartfarming.zone.Zone;

import java.util.Date;

public class CapteurEnvironnemental extends CapteurNumerique {
    private float temperature;
    private float humidite;
    private float pluviometrie;

    public CapteurEnvironnemental(String code, Zone zone, double seuilMin, double seuilMax, String unite,
                                   float temperature, float humidite, float pluviometrie) {
        super(code, zone, seuilMin, seuilMax, unite);
        this.temperature = temperature;
        this.humidite = humidite;
        this.pluviometrie = pluviometrie;
    }

    public float getTemperature() { return temperature; }
    public void setTemperature(float temperature) { this.temperature = temperature; }

    public float getHumidite() { return humidite; }
    public void setHumidite(float humidite) { this.humidite = humidite; }

    public float getPluviometrie() { return pluviometrie; }
    public void setPluviometrie(float pluviometrie) { this.pluviometrie = pluviometrie; }

    @Override
    public ReleveNumerique envoyerReleve() {
        NiveauReleve niveau = verifierSeuil(temperature) ? NiveauReleve.NORMAL : NiveauReleve.AVERTISSEMENT;
        ReleveNumerique releve = new ReleveNumerique(historique.size() + 1, this, new Date(), niveau, temperature, unite, this);
        historique.add(releve);
        return releve;
    }
}
