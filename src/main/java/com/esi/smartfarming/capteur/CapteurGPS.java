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

    public CapteurGPS(String code, Zone zone, Animal animal) {
        super(code, zone);
        this.animal = animal;
        this.historiqueGPS = new ArrayList<>();
    }

    public Animal getAnimal() { return animal; }
    public List<ReleveGPS> getHistoriqueGPS() { return historiqueGPS; }

    public ReleveGPS envoyerReleve() {
        double latitude = 36.7 + (Math.random() * 0.05);
        double longitude = 3.05 + (Math.random() * 0.05);
        ReleveGPS releve = new ReleveGPS(historiqueGPS.size() + 1, this, new Date(), NiveauReleve.NORMAL, latitude, longitude, this);
        historiqueGPS.add(releve);
        return releve;
    }
}
