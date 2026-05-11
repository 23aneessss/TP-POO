package com.esi.smartfarming.zone;

import com.esi.smartfarming.alimentation.ProgrammeAlimentation;
import com.esi.smartfarming.animal.EspeceAquacole;
import com.esi.smartfarming.capteur.CapteurEau;
import com.esi.smartfarming.historique.HistoriqueAquacole;

import java.util.ArrayList;
import java.util.List;


public class ZoneAquacole extends Zone {
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
}
