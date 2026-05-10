package com.esi.smartfarming.capteur;

import com.esi.smartfarming.animal.Animal;
import com.esi.smartfarming.enums.NiveauReleve;
import com.esi.smartfarming.releve.ReleveGPS;
import com.esi.smartfarming.zone.Zone;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CapteurGPS extends Capteur {
    private Animal animal;
    private List<ReleveGPS> historiqueGPS;

    public CapteurGPS(String code, Zone zone, double seuilMin, double seuilMax, Animal animal) {
        super(code, zone, seuilMin, seuilMax);
        this.animal = animal;
        this.historiqueGPS = new ArrayList<>();
    }

    public List<ReleveGPS> getHistoriqueGPS() { return historiqueGPS; }

    public ReleveGPS envoyerReleve() {
        ReleveGPS releve = new ReleveGPS(historiqueGPS.size() + 1, this, new Date(), NiveauReleve.NORMAL, 0.0, 0.0, this);
        historiqueGPS.add(releve);
        return releve;
    }
}
