package com.esi.smartfarming.zone;

import com.esi.smartfarming.capteur.Capteur;
import com.esi.smartfarming.core.Suspendable;
import com.esi.smartfarming.enums.StatutZone;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public abstract class Zone implements Suspendable {
    protected String code;
    protected String nom;
    protected StatutZone statut;
    protected List<Capteur> capteurs;

    protected Zone(String code, String nom) {
        this.code = code;
        this.nom = nom;
        this.statut = StatutZone.ACTIVE;
        this.capteurs = new ArrayList<>();
    }

    @Override
    public void suspendre() {}

    @Override
    public void activer() {}
}
