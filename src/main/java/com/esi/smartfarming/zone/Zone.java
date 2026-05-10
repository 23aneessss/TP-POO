package com.esi.smartfarming.zone;

import com.esi.smartfarming.capteur.Capteur;
import com.esi.smartfarming.core.Suspendable;
import com.esi.smartfarming.enums.StatutZone;
import com.esi.smartfarming.historique.HistoriqueProduction;

import java.util.ArrayList;
import java.util.List;

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

    public String getCode() { return code; }
    public String getNom() { return nom; }
    public StatutZone getStatut() { return statut; }
    public List<Capteur> getCapteurs() { return capteurs; }

    @Override
    public void suspendre() { this.statut = StatutZone.SUSPENDUE; }

    @Override
    public void activer() { this.statut = StatutZone.ACTIVE; }

    protected abstract HistoriqueProduction getHistorique();
}
