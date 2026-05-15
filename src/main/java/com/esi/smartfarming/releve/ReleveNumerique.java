package com.esi.smartfarming.releve;

import com.esi.smartfarming.capteur.Capteur;
import com.esi.smartfarming.capteur.CapteurNumerique;
import com.esi.smartfarming.enums.NiveauReleve;

import java.util.Date;

public class ReleveNumerique extends Releve {
    private double valeur;
    private String unite;
    private CapteurNumerique capteurNumerique;

    public ReleveNumerique(int id, Capteur capteur, Date dateHeure, NiveauReleve niveau, double valeur, String unite, CapteurNumerique capteurNumerique) {
        super(id, capteur, dateHeure, niveau);
        this.valeur = valeur;
        this.unite = unite;
        this.capteurNumerique = capteurNumerique;
    }

    public double getValeur() { return valeur; }
    public String getUnite() { return unite; }
    public CapteurNumerique getCapteurNumerique() { return capteurNumerique; }
}
