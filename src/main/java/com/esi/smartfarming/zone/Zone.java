package com.esi.smartfarming.zone;

import com.esi.smartfarming.capteur.Capteur;
import com.esi.smartfarming.core.Suspendable;
import com.esi.smartfarming.enums.StatutZone;
import com.esi.smartfarming.historique.HistoriqueProduction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe abstraite représentant une zone géographique de la ferme intelligente.
 * <p>
 * Chaque zone possède un code unique, un nom, un statut (ACTIVE / SUSPENDUE),
 * et une liste de capteurs qui lui sont rattachés.
 * Les sous-classes concrètes spécialisent le type de production :
 * <ul>
 *   <li>{@link ZoneCulture}  — cultures végétales</li>
 *   <li>{@link ZoneElevage}  — élevage animal</li>
 *   <li>{@link ZoneAquacole} — aquaculture</li>
 * </ul>
 *
 * @see com.esi.smartfarming.core.Suspendable
 */
public abstract class Zone implements Suspendable, Serializable {
    private static final long serialVersionUID = 1L;
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
    public void setNom(String nom) { this.nom = nom; }
    public StatutZone getStatut() { return statut; }
    public List<Capteur> getCapteurs() { return capteurs; }

    @Override
    public void suspendre() { this.statut = StatutZone.SUSPENDUE; }

    @Override
    public void activer() { this.statut = StatutZone.ACTIVE; }

    public abstract HistoriqueProduction getHistorique();
}
