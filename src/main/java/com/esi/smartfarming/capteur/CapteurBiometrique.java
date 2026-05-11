package com.esi.smartfarming.capteur;

import com.esi.smartfarming.animal.Animal;
import com.esi.smartfarming.zone.Zone;

@SuppressWarnings("unused")
public class CapteurBiometrique extends CapteurNumerique {
    private Animal animal;
    private String typeCapture;

    public CapteurBiometrique(String code, Zone zone, double seuilMin, double seuilMax, String unite, Animal animal, String typeCapture) {
        super(code, zone, seuilMin, seuilMax, unite);
        this.animal = animal;
        this.typeCapture = typeCapture;
    }
}
