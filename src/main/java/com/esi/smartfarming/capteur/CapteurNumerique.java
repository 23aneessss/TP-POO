package com.esi.smartfarming.capteur;

import com.esi.smartfarming.enums.NiveauReleve;
import com.esi.smartfarming.releve.ReleveNumerique;
import com.esi.smartfarming.zone.Zone;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class CapteurNumerique extends Capteur {
    protected String unite;
    protected List<ReleveNumerique> historique;

    protected CapteurNumerique(String code, Zone zone, double seuilMin, double seuilMax, String unite) {
        super(code, zone, seuilMin, seuilMax);
        this.unite = unite;
        this.historique = new ArrayList<>();
    }

    public String getUnite() { return unite; }
    public List<ReleveNumerique> getHistorique() { return historique; }

    public abstract ReleveNumerique envoyerReleve();

    protected boolean verifierSeuil(double valeur) {
        return valeur >= seuilMin && valeur <= seuilMax;
    }

    protected ReleveNumerique creerEtEnregistrerReleve(double valeur) {
        NiveauReleve niveau = verifierSeuil(valeur) ? NiveauReleve.NORMAL : NiveauReleve.CRITIQUE;
        ReleveNumerique releve = new ReleveNumerique(historique.size() + 1, this, new Date(), niveau, valeur, unite, this);
        historique.add(releve);
        return releve;
    }
}
