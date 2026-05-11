package com.esi.smartfarming.capteur;

import com.esi.smartfarming.zone.Zone;

public class CapteurEau extends CapteurNumerique {
    private String typeCapture;

    public CapteurEau(String code, Zone zone, double seuilMin, double seuilMax, String unite, String typeCapture) {
        super(code, zone, seuilMin, seuilMax, unite);
        this.typeCapture = typeCapture;
    }

    public String getTypeCapture() { return typeCapture; }
    public void setTypeCapture(String typeCapture) { this.typeCapture = typeCapture; }
}
