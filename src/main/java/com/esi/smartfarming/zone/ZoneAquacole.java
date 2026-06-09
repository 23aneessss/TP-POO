package com.esi.smartfarming.zone;

import com.esi.smartfarming.alimentation.ProgrammeAlimentation;
import com.esi.smartfarming.animal.EspeceAquacole;
import com.esi.smartfarming.capteur.CapteurEau;
import com.esi.smartfarming.historique.HistoriqueAquacole;
import com.esi.smartfarming.historique.HistoriqueProduction;

import java.util.ArrayList;
import java.util.List;

/**
 * Zone dédiée à l'aquaculture (bassins de poissons, crustacés…).
 * <p>
 * Gère une liste d'{@link com.esi.smartfarming.animal.EspeceAquacole},
 * des capteurs d'eau ({@link com.esi.smartfarming.capteur.CapteurEau})
 * et un {@link com.esi.smartfarming.alimentation.ProgrammeAlimentation}.
 * Une zone aquacole est initialement suspendue si les paramètres d'eau
 * ne sont pas encore conformes.
 */
public class ZoneAquacole extends Zone {
    private static final long serialVersionUID = 1L;
    private List<EspeceAquacole> especes;
    private List<CapteurEau> capteursEau;
    private ProgrammeAlimentation programmeAlimentation;
    private HistoriqueAquacole historique;

    public ZoneAquacole(String code, String nom, ProgrammeAlimentation programmeAlimentation) {
        super(code, nom);
        this.especes = new ArrayList<>();
        this.capteursEau = new ArrayList<>();
        this.programmeAlimentation = programmeAlimentation;
    }

    public void ajouterEspece(EspeceAquacole e) {
        this.especes.add(e);
    }

    public List<EspeceAquacole> getEspeces() { return especes; }

    public void ajouterCapteurEau(CapteurEau c) {
        this.capteursEau.add(c);
        this.capteurs.add(c);
    }

    public void setProgrammeAlimentation(ProgrammeAlimentation p) { this.programmeAlimentation = p; }
    public ProgrammeAlimentation getProgrammeAlimentation() { return programmeAlimentation; }

    @Override
    public HistoriqueProduction getHistorique() { return historique; }
}
