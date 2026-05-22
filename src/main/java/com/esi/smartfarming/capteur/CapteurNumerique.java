package com.esi.smartfarming.capteur;

import com.esi.smartfarming.releve.ReleveNumerique;
import com.esi.smartfarming.zone.Zone;

import java.util.ArrayList;
import java.util.List;

public abstract class CapteurNumerique extends Capteur {
    private static final long serialVersionUID = 1L;
    protected String unite;
    protected List<ReleveNumerique> historique;

    protected CapteurNumerique(String code, Zone zone, String unite) {
        super(code, zone);
        this.unite = unite;
        this.historique = new ArrayList<>();
    }

    public String getUnite() { return unite; }
    public List<ReleveNumerique> getHistorique() { return historique; }

    public abstract ReleveNumerique envoyerReleve();
}
