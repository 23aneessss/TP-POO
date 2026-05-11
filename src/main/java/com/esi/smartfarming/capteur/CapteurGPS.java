package com.esi.smartfarming.capteur;

import com.esi.smartfarming.animal.Animal;
import com.esi.smartfarming.releve.ReleveGPS;
import com.esi.smartfarming.zone.Zone;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class CapteurGPS extends Capteur {
    private Animal animal;
    private List<ReleveGPS> historiqueGPS;

    public CapteurGPS(String code, Zone zone, double seuilMin, double seuilMax, Animal animal) {
        super(code, zone, seuilMin, seuilMax);
        this.animal = animal;
        this.historiqueGPS = new ArrayList<>();
    }
}
