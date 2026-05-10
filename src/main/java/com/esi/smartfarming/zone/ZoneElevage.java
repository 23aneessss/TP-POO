package com.esi.smartfarming.zone;

import com.esi.smartfarming.alimentation.ProgrammeAlimentation;
import com.esi.smartfarming.animal.Animal;
import com.esi.smartfarming.historique.HistoriqueLait;
import com.esi.smartfarming.historique.HistoriqueOeuf;
import com.esi.smartfarming.historique.HistoriqueProduction;

import java.util.ArrayList;
import java.util.List;

public class ZoneElevage extends Zone {
    private List<Animal> animaux;
    private ProgrammeAlimentation programmeAlimentation;
    private HistoriqueLait historiqueLait;
    private HistoriqueOeuf historiqueOeuf;

    public ZoneElevage(String code, String nom, ProgrammeAlimentation programmeAlimentation) {
        super(code, nom);
        this.animaux = new ArrayList<>();
        this.programmeAlimentation = programmeAlimentation;
    }

    public void ajouterAnimal(Animal a) { this.animaux.add(a); }
    public List<Animal> getAnimaux() { return animaux; }
    public void setProgrammeAlimentation(ProgrammeAlimentation p) { this.programmeAlimentation = p; }
    public ProgrammeAlimentation getProgrammeAlimentation() { return programmeAlimentation; }

    @Override
    protected HistoriqueProduction getHistorique() {
        return historiqueLait != null ? historiqueLait : historiqueOeuf;
    }
}
