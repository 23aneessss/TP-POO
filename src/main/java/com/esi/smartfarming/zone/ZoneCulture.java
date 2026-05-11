package com.esi.smartfarming.zone;

import com.esi.smartfarming.capteur.CapteurEnvironnemental;
import com.esi.smartfarming.capteur.CapteurSol;
import com.esi.smartfarming.culture.Culture;
import com.esi.smartfarming.historique.HistoriqueCulture;

import java.util.ArrayList;
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
}
