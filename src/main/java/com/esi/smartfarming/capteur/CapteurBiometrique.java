package com.esi.smartfarming.capteur;

import com.esi.smartfarming.animal.Animal;
import com.esi.smartfarming.zone.Zone;

public class CapteurBiometrique extends CapteurNumerique {
    private Animal animal;
    private String typeCapture;

    public CapteurBiometrique(String code, Zone zone, double seuilMin, double seuilMax, String unite, Animal animal, String typeCapture) {
        super(code, zone, seuilMin, seuilMax, unite);
        this.animal = animal;
        this.typeCapture = typeCapture;
    }

    public Animal getAnimal() { return animal; }
    public void setAnimal(Animal animal) { this.animal = animal; }

    public String getTypeCapture() { return typeCapture; }
    public void setTypeCapture(String typeCapture) { this.typeCapture = typeCapture; }
}
