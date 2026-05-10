package com.esi.smartfarming.zone;

import com.esi.smartfarming.capteur.CapteurEnvironnemental;
import com.esi.smartfarming.capteur.CapteurSol;
import com.esi.smartfarming.culture.Culture;
import com.esi.smartfarming.historique.HistoriqueCulture;
import com.esi.smartfarming.historique.HistoriqueProduction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ZoneCulture extends Zone {
    private List<Culture> cultures;
    private List<CapteurEnvironnemental> capteursEnv;
    private List<CapteurSol> capteursSol;
    private HistoriqueCulture historique;

    public ZoneCulture(String code, String nom) {
        super(code, nom);
        this.cultures = new ArrayList<>();
        this.capteursEnv = new ArrayList<>();
        this.capteursSol = new ArrayList<>();
    }

    public void ajouterCulture(Culture c) { this.cultures.add(c); }
    public List<Culture> getCultures() { return cultures; }

    public String genererRapportCultures() {
        return "Zone " + nom + " contient " + cultures.size() + " culture(s).";
    }

    public void enregistrerRendement(double valeur, Date date) {
        this.historique = new HistoriqueCulture(this, date, valeur);
        this.historique.enregistrer();
    }

    @Override
    protected HistoriqueProduction getHistorique() { return historique; }
}
