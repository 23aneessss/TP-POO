package com.esi.smartfarming.capteur;

import com.esi.smartfarming.animal.Animal;
import com.esi.smartfarming.releve.ReleveGPS;
import com.esi.smartfarming.zone.Zone;

import java.util.ArrayList;
import java.util.List;

public class CapteurGPS extends Capteur {
    private Animal animal;
    private List<ReleveGPS> historiqueGPS;

    public CapteurGPS(String code, Zone zone, double seuilMin, double seuilMax, Animal animal) {
        super(code, zone, seuilMin, seuilMax);
        this.animal = animal;
        this.historiqueGPS = new ArrayList<>();
    }

    public Animal getAnimal() { return animal; }
    public void setAnimal(Animal animal) { this.animal = animal; }

    public List<ReleveGPS> getHistoriqueGPS() { return historiqueGPS; }
    public void setHistoriqueGPS(List<ReleveGPS> historiqueGPS) { this.historiqueGPS = historiqueGPS; }
}
