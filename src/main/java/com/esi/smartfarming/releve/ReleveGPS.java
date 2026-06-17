package com.esi.smartfarming.releve;

import com.esi.smartfarming.capteur.Capteur;
import com.esi.smartfarming.capteur.CapteurGPS;
import com.esi.smartfarming.enums.NiveauReleve;

import java.util.Date;

public class ReleveGPS extends Releve {
    private static final long serialVersionUID = 1L;
    private double latitude;
    private double longitude;
    private CapteurGPS capteurGPS;

    public ReleveGPS(int id, Capteur capteur, Date dateHeure, NiveauReleve niveau, double latitude, double longitude, CapteurGPS capteurGPS) {
        super(id, capteur, dateHeure, niveau);
        this.latitude = latitude;
        this.longitude = longitude;
        this.capteurGPS = capteurGPS;
    }

    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public CapteurGPS getCapteurGPS() { return capteurGPS; }
}
